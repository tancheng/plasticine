/*
    Accel {
      Sequential(iters by 1){ iter =>  //s1
        Sequential(NP by tileSize) { tid => //s2
          Pipe {initPR := OCpages(tid::tid+tileSize) } //tl1
          Pipe {edgesId := OCedgeId(tid :: tid+tileSize) } //tl2
          Pipe {edgesLen := OCedgeLen(tid :: tid+tileSize) } //tl3
          Sequential(tileSize by 1) { pid => //s3
            Pipe{numEdges := edgesLen(pid)} //p1
            Parallel { // par1
              Pipe {edges := OCedges(startId::startId+numEdges.value) }  //tl4
              Pipe {counts := OCcounts(startId::startId+numEdges.value) } //tl5
            }
            Sequential(numEdges.value by 1){ i => //s4
              val addr = edges(i) // p2
              val onchip = addr >= tid && addr < tid+tileSize // p3
              offAddr := offAddr.value + mux(onchip, 0, 1) // p4
              offLoc(i) = mux(onchip, offAddr.value, -1.as[SInt]) // p5
            }
            Sequential(numEdges.value by 1){} // p6
            Pipe {gatheredPR := OCpages(frontierOff, offAddr.value)} //g1
            val pr = Reduce(numEdges.value by 1)(0.as[T]){ i => // p7
              val addr = edges(i)  // primitive
              val off  = offLoc(i)  // primitive
              val onchipRank = pageRank(addr - tid)  // primitive
              val offchipRank = gatheredPR(off)  // primitive
              val rank = mux(off == -1.as[SInt], onchipRank, offchipRank)  // primitive
              rank / counts(i)  // primitive
            }{_+_}
            currentPR(pid) = pr.value * damp + (1.as[T] - damp) // p8
          }
          OCpages(tid::tid+tileSize) := currentPR // ts1
        }
      }
    }
    getMem(OCpages)
  }
*/