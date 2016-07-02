

object GDA {

  //mu0Tile := mu0(0::Cmax, subLoopPar)  // Load mu0
  MemCtrl (id=0) (dram=DRAM("mu0")) {
    val cc = SM(0 until Cmax)
  }
  //mu1Tile := mu1(0::Cmax, subLoopPar)  // Load mu1
  MemCtrl (id=1) (dram=DRAM("mu1")) {
    val cc = SM(0 until Cmax)
  }
  
  //Pipe.fold(rows by rTileSize par outerPar, outerAccumPar)(sigmaOut){ r =>
  //}{_+_}
  ComputeUnit (id=0) {
    // StateMachines / CounterChain
    val r = SM(rows by rTileSize) //Local
    val acc = SM(sigmaOutWidth by 1) //BlockReduce
    val rr = SM.copy(ComputeUnit(2), "rr")

    // SRAMs
    val sigmaBlk    = SRAM(write = ComputeUnit(1), readAddr = acc(0) , writeAddr = rr(0))
    val sigmaOut = SRAM(write = local, readAddr = acc(0) , writeAddr = acc(0))

    // Compute Unit
    val x   = PR(-1).load(sigmaTile)
    val y   = PR(-1).load(sigmaOut)
    Stage (op1=x, op2=y, op=sum, result=PR.store(sigmaOut))
  }

  //yTile := y(r::r+rTileSize, subLoopPar)
  MemCtrl (id=2, dram=DRAM("y")) {
    val r = SM.copy(ComputeUnit(0), "r")
    val rr = SM(r until r+rTileSize by 1, dep=r, type=pipeline)
  }

  //xTile := x(r::r+rTileSize, 0::cols, subLoopPar)  // Load tile of x
  MemCtrl (id=3, dram=DRAM("x")) {
    val r = SM.copy(ComputeUnit(0), "r")
    val rr = SM(r until r+rTileSize by 1, cols by 1, dep=r, type=pipeline)
  }

  //Pipe.fold(rTileSize par innerPar, prodLoopPar)(sigmaBlk){rr =>
  //}{_+_}
  ComputeUnit (id=1) {
    // StateMachines / CounterChain
    val rr = SM(rTileSize par innerPar, dep=SM(ComputeUnit(0), "r"), type=pipeline) //Local
    val acc = SM(sigmaBlkSize by 1) //BlockReduce
    val c1 = SM.copy(ComputeUnit(3), "c1")

    // SRAMs
    val sigmaTile    = SRAM(write = ComputeUnit(3), readAddr = acc(0) , writeAddr = PR(stage="sigmaTileWA", id=2))
    val sigmaBlk = SRAM(write = local, readAddr = acc(0) , writeAddr = acc(0))

    // Remote Addr Calc for sigmaTile 
    val temp1 = PR.temp
    val temp2 = PR.temp
    val j = PR(-1).c1(0)
    val i = PR(-1).c1(1)
    Stage (op1=i, op2=Const(sigmaTileWidth), op=product, result=temp1)
    Stage (name="sigmaTileWA", op1=temp1, op2=j, op=sum, result=temp2)

    // Accumulate
    val x   = PR(-3).load(sigmaTile) // sigmaTile.read
    val y   = PR(-3).load(sigmaOut) //sigmaOut.read
    Stage (op1=x, op2=y, op=sum, result=PR.store(sigmaBlk))
  }
  
  //Pipe(cols par subLoopPar){ cc =>
  //  subTile(cc) = xTile(rr,cc) - mux(yTile(rr), mu1Tile(cc), mu0Tile(cc))
  //}
  ComputeUnit (id=2) {
    // StateMachines / CounterChain
    val rr = SM.copy(ComputeUnit(1), rr)
    val cc = SM(cols by 1, dep=rr, type=pipeline) //Local
    val cmu0 = SM.copy(MemCtrl(0), "cc")
    val cmu1 = SM.copy(MemCtrl(1), "cc")
    val cy = SM.copy(MemCtrl(2), "rr")
    val cx = SM.copy(MemCtrl(3), "rr")

    // SRAMs
    val xTile   = SRAM(write = MemCtrl(3), readAddr = PR(stage="xTileRA", id="xTileRA") , writeAddr = PR(stage="xTileWA", id="xTileRA"))
    val yTile = SRAM(write = MemCtrl(2), readAddr = rr(0), writeAddr = cy(0))
    val mu0Tile = SRAM(write = MemCtrl(0), readAddr = cc(0), writeAddr = cmu0(0))
    val mu1Tile = SRAM(write = MemCtrl(1), readAddr = cc(0), writeAddr = cmu1(0))

    // Compute Unit
    // Remote Addr Calc for xTile
    val temp1 = PR.temp
    Stage (op1=PR(-1).cx(0), op2=Const(xTileWidth), op=product, result=temp1)
    Stage (name="xTileWA", op1=temp1, op2=PR(-1).cx(1), op=sum, result=PR.temp("xTileWA"))
    // xTile readAddr calculation
    val temp2 = PR.temp
    Stage (op1=PR(-3).rr(0), op2=Const(xTileWidth), op=product, result=temp2)
    Stage (name="xTileRA", op1=temp2, op2=PR(-3).cc(0), op=sum, result=PR.temp("xTileRA"))
    // Compute
    val y   = PR(-5).load(yTile)
    val x   = PR(-5).load(xTile)
    val mu1 = PR(-5).load(mu1)
    val mu0 = PR(-5).load(mu0)
    val temp3 = PR.temp
    Stage (op1=mu1, op2=mu0, op3=y, op=mux, result=temp3)
    Stage (op1=x, op2=temp3, op=sub, result=PR.vecOut)
  }

  //Pipe(cols by 1, cols par prodLoopPar){ (ii,jj) =>
  //  sigmaTile(ii,jj) = subTile(ii) * subTile(jj)
  //}
  ComputeUnit (id=3) {
    // StateMachines / CounterChain
    val c2 = SM.copy(ComputeUnit(2), "cc")
    val c1 = SM(cols by 1, cols by 1, dep=c2, type=sequential) //Local, (i,j)

    // SRAMs
    val subTile_ra   = SRAM(write=ComputeUnit(2), readAddr=c1(0), writeAddr=c2(0))
    val subTile_ca   = SRAM(write=ComputeUnit(2), readAddr=c1(1), writeAddr=c2(0))

    // Compute Unit
    val a = PR(-1).load(subTile_ra) // val a = subTile_ra.read
    val b = PR(-1).load(subTile_ca) // val b = subTile_ca.read
    Stage (op1=a, op2=b, op=product, result=PR.vecOut)
  }


}
