
object DotProduct {
  //Pipe.reduce(tileSize par innerPar)(Reg[T]){ii => b2(ii) * b2(ii) }{_+_}

  ComputeUnit (id=0) {
    // StateMachines / CounterChain
    val c1 = SM(tileSize by 1) //Local
    val c2 = SM.copy(MemCtrl("A"), "cc")
    val c3 = SM.copy(MemCtrl("B"), "cc")

    // SRAMs
    val A = SRAM(write=remote, readAddr=c1(0), writeAddr=c2(0))
    val B = SRAM(write=remote, readAddr=c1(0), writeAddr=c3(0))

    // Compute Unit
    val a = PR(-1).load(A) // PR(-1) is PR of previous stage. For first stage is the forwarding path from SRAM to FU
    val b = PR(-1).load(B)
    Stage (op1=a, op2=b, op=product, result=PR.reduce)
    Stage.reduce(_+_) // Infer log2(par) + 1 stages used for reduction and result stored to PR.reduce
    Stage.(op1=PR.reduce, op=bypass, result=PR.scalarOut(0)) 
    //Last stage can be removed if PR.reduce and PR.scalarOut map to the same register
  }

}

