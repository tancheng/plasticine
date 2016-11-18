/*
    Accel { // top
      Parallel { //par1
        Pipe{frontierIds := OCids(0::tileSize)} // tl1
        Pipe{frontierCounts := OCcounts(0::tileSize)} // tl2
      }
      Sequential(4 by 1) { i => //s1
        Fold(numEdges.value by 1)(concatReg, 0.as[SInt]) { k => // m1
          Pipe{ // p3
            fetch := currentNodes(k) // primitive
            lastFetch := currentNodes(k-1) // primitive
          }
          Pipe{ // p4
            nextId := frontierIds(fetch)  // primitive
            nextLen := frontierCounts(fetch)  // primitive
            lastLen := frontierCounts(lastFetch)  // primitive
          }
          Pipe{pieceMem := OCedges(nextId :: nextId + nextLen.value)} //tl3
          Pipe(nextLen.value by 1) { kk => // p5
            val plus = mux(k == 0, 0, lastLen.value)  // primitive
            frontierAddr(kk) = kk + concatReg.value + plus  // primitive
          }

          Pipe(nextLen.value by 1) { kk => // p6
            frontierNodes(frontierAddr(kk)) = pieceMem(kk) // primitive
          }
          nextLen
        }{_+_} // p7
        Pipe{concatReg.value by 1} { kk => currentNodes(kk) = frontierNodes(kk)} // p8
        Pipe(concatReg.value by 1) { k => frontierLevels(k) = i+1 } // p9
        OCresult(currentNodes, concatReg.value) := frontierLevels // scat1
        Pipe{numEdges := concatReg.value} // p10
      }
    }
    getMem(OCresult)
  }
*/
