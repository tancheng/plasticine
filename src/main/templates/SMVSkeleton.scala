
    Accel {
      Pipe(N by tileSize par op){ rowchunk => //m1
        tileSizes := sizes(rowchunk :: rowchunk+tileSize par ip) //tl1
        Sequential(tileSize by 1){row => //m2

          val len = tileSizes(row) //p1
          val OCROW = (rowchunk+row) // p2
          Parallel { //par1
            csrCols := aC(OCROW, 0 :: len par ip) // tl2
            csrData := aD(OCROW, 0 :: len par ip) // tl3
          }
          vecGathered := v(csrCols, len) //g1

          val acc = Reduce(len by 1)(0.as[T]) { i => // p3
            csrData(i) * vecGathered(i) // primitive
          }{_+_}

          result(row) = acc.value // p4

        }
      out(rowchunk::rowchunk+tileSize par stPar) := result //ts1
      }
    }
    getMem(out)
  }
*/