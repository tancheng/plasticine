{"PISA":{
"version"     : 0.1,
"topconfig" : {
  "type" : "plasticine",
  "config" : {
    "cu" : [{
      "scratchpads" : [
        {"ra" : "i0", "wa": "x", "wd" : "x", "wen" : "x"},
        {"ra" : "i0", "wa": "x", "wd" : "x", "wen" : "x"}
      ],
      "counterChain" : {
        "chain" : [0],
        "counters" : [
          {
            "max"         : 64,
            "stride"      : 1,
            "maxConst"    : 1,
            "strideConst" : 1,
            "startDelay"  : 0,
            "endDelay"    : 3
          },
          {
            "max"         : 0,
            "stride"      : 0,
            "maxConst"    : 0,
            "strideConst" : 0,
            "startDelay" :  0,
            "endDelay" : 0
          }
        ]
      },
      "pipeStage" : [
        {
          "opA" : "m0",
          "opB" : "m1",
          "opcode" : "*",
          "result" : "l1",
          "fwd"   : { }
        },
        {
          "opA" : "r0",
          "opB" : "l0",
          "opcode" : "passA",
          "result" : "l2",
          "fwd"   : { }
        }
      ],
     "control" : {
      "tokenOutLUT" : [ {"table" : [0,1,0,1]},
                        {"table" : [0,0,0,0]}],

      "incXbar" : {"outSelect": [0,x,x,x]},
      "decXbar" : {"outSelect": [0,x]},
      "udcInit" : [0,0],

      "enableLUT" : [   {"table" : [0,1,0,1]},
                        {"table" : [0,0,0,0]}]
    }
    },
    {
      "scratchpads" : [
        {"ra": "x", "wa": "i1", "wd" : "remote", "wen" : "i1"},
        {"ra": "x", "wa": "x", "wd" : "x", "wen" : "x"}
      ],
      "counterChain" : {
        "chain" : [0],
        "counters" : [
          {
            "max"         : 5,
            "stride"      : 1,
            "maxConst"    : 1,
            "strideConst" : 1,
            "startDelay" :  0,
            "endDelay" : 0
          },
          {
            "max"         : 64,
            "stride"      : 1,
            "maxConst"    : 1,
            "strideConst" : 1,
            "startDelay" :  3,
            "endDelay" : 0
          }
        ]
      },
      "pipeStage" : [
        {
          "opA" : "r0",
          "opB" : "r1",
          "opcode" : "*",
          "result" : "l3",
          "fwd"   : {"r0" : "counter", "r2" : "memory" }
        },
        {
          "opA" : "r1",
          "opB" : "l1",
          "opcode" : "+",
          "result" : "l1",
          "fwd"   : { }
        }
      ],
     "control" : {
      "tokenOutLUT" : [ {"table" : [0,0,0,0]},
                        {"table" : [0,0,0,0]}],

      "incXbar" : {"outSelect": [0,1,0,0]},
      "decXbar" : {"outSelect": [0,2]},
      "udcInit" : [0,0],

      "enableLUT" : [   {"table" : [0,0,0,0]},
                        {"table" : [0,0,1,1]}]

    }
    }
  ]
  }
}
}
}
