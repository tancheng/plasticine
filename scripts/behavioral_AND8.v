
module behavioral_AND8 ( io_a, io_b, io_opcode, reg_add_test, io_out, 
        reg_value );
  input [3:0] io_a;
  input [3:0] io_b;
  input [1:0] io_opcode;
  input [7:0] reg_add_test;
  output [3:0] io_out;
  output [7:0] reg_value;
  wire   n37, n38, n39, n40, n41, n42, n43, n44, n45, n46, n47, n48, n49, n50,
         n51, n52, n53, n54, n55, n56, n57, n58, n59, n60, n61, n62, n63, n64,
         n65, n66, n67, n68, n69, n70, n71, n72, n73, n74, n75, n76, n77, n78,
         n79, n80, n81, n82, n83, n84, n85, n86, n87, n88, n89, n90, n91, n92,
         n93, n94, n95, n96;

  MAOI222D1BWP U42 ( .A(n86), .B(io_a[2]), .C(io_b[2]), .ZN(n91) );
  INVD1BWP U43 ( .I(reg_add_test[0]), .ZN(n65) );
  INVD1BWP U44 ( .I(reg_add_test[1]), .ZN(n64) );
  INVD1BWP U45 ( .I(reg_add_test[2]), .ZN(n63) );
  INVD1BWP U46 ( .I(reg_add_test[3]), .ZN(n62) );
  INVD1BWP U47 ( .I(reg_add_test[4]), .ZN(n61) );
  INVD1BWP U48 ( .I(reg_add_test[5]), .ZN(n60) );
  INVD1BWP U49 ( .I(reg_add_test[6]), .ZN(n59) );
  INVD1BWP U50 ( .I(reg_add_test[7]), .ZN(n58) );
  OA222D1BWP U51 ( .A1(n80), .A2(n79), .B1(n78), .B2(io_opcode[1]), .C1(n77), 
        .C2(n76), .Z(n81) );
  XNR2D1BWP U52 ( .A1(io_b[3]), .A2(io_a[3]), .ZN(n37) );
  AO22D1BWP U53 ( .A1(n92), .A2(n87), .B1(n90), .B2(n88), .Z(n38) );
  CKND2D1BWP U54 ( .A1(n37), .A2(n38), .ZN(n39) );
  OAI21D1BWP U55 ( .A1(n37), .A2(n93), .B(n39), .ZN(n94) );
  OA21D1BWP U56 ( .A1(n78), .A2(n42), .B(io_a[1]), .Z(n40) );
  CKND1BWP U57 ( .I(io_a[0]), .ZN(n77) );
  ND2D2BWP U58 ( .A1(io_b[0]), .A2(n77), .ZN(n78) );
  CKND1BWP U59 ( .I(io_b[1]), .ZN(n42) );
  ND2D1BWP U60 ( .A1(n78), .A2(n42), .ZN(n41) );
  INR2D2BWP U61 ( .A1(n41), .B1(n40), .ZN(n85) );
  CKND1BWP U62 ( .I(n85), .ZN(n45) );
  CKND1BWP U63 ( .I(io_opcode[0]), .ZN(n50) );
  NR2D1BWP U64 ( .A1(n50), .A2(io_opcode[1]), .ZN(n90) );
  CKND2D1BWP U65 ( .A1(io_b[0]), .A2(io_a[0]), .ZN(n68) );
  CKND2D1BWP U66 ( .A1(io_a[1]), .A2(io_b[1]), .ZN(n43) );
  CKND1BWP U67 ( .I(io_a[1]), .ZN(n69) );
  AOI22D1BWP U68 ( .A1(n68), .A2(n43), .B1(n42), .B2(n69), .ZN(n86) );
  CKND1BWP U69 ( .I(n86), .ZN(n44) );
  NR2D1BWP U70 ( .A1(io_opcode[1]), .A2(io_opcode[0]), .ZN(n92) );
  AOI22D1BWP U71 ( .A1(n45), .A2(n90), .B1(n44), .B2(n92), .ZN(n56) );
  CKND1BWP U72 ( .I(io_b[2]), .ZN(n51) );
  CKND1BWP U73 ( .I(io_a[2]), .ZN(n84) );
  ND2D1BWP U74 ( .A1(n51), .A2(n84), .ZN(n54) );
  CKND2D1BWP U75 ( .A1(io_b[2]), .A2(io_a[2]), .ZN(n46) );
  ND2D1BWP U76 ( .A1(n54), .A2(n46), .ZN(n48) );
  ND2D1BWP U77 ( .A1(n86), .A2(n92), .ZN(n47) );
  ND2D1BWP U78 ( .A1(n48), .A2(n47), .ZN(n49) );
  AOI21D1BWP U79 ( .A1(n85), .A2(n90), .B(n49), .ZN(n53) );
  ND2D1BWP U80 ( .A1(io_opcode[1]), .A2(n50), .ZN(n75) );
  OAI21D1BWP U81 ( .A1(n53), .A2(n51), .B(n75), .ZN(n52) );
  ND2D1BWP U82 ( .A1(io_opcode[1]), .A2(n75), .ZN(n80) );
  MAOI22D1BWP U83 ( .A1(n52), .A2(io_a[2]), .B1(n51), .B2(n80), .ZN(n55) );
  AOI32D1BWP U84 ( .A1(n56), .A2(n55), .A3(n54), .B1(n53), .B2(n55), .ZN(n57)
         );
  BUFFD12BWP U85 ( .I(n57), .Z(io_out[2]) );
  INVD8BWP U86 ( .I(n58), .ZN(reg_value[7]) );
  INVD8BWP U87 ( .I(n59), .ZN(reg_value[6]) );
  INVD8BWP U88 ( .I(n60), .ZN(reg_value[5]) );
  INVD8BWP U89 ( .I(n61), .ZN(reg_value[4]) );
  INVD8BWP U90 ( .I(n62), .ZN(reg_value[3]) );
  INVD8BWP U91 ( .I(n63), .ZN(reg_value[2]) );
  INVD8BWP U92 ( .I(n64), .ZN(reg_value[1]) );
  INVD8BWP U93 ( .I(n65), .ZN(reg_value[0]) );
  CKND1BWP U94 ( .I(n75), .ZN(n82) );
  CKND1BWP U95 ( .I(n80), .ZN(n83) );
  AO22D1BWP U96 ( .A1(io_a[1]), .A2(n82), .B1(io_b[1]), .B2(n83), .Z(n74) );
  CKND1BWP U97 ( .I(n78), .ZN(n67) );
  CKND1BWP U98 ( .I(n68), .ZN(n66) );
  AOI22D1BWP U99 ( .A1(n67), .A2(n90), .B1(n66), .B2(n92), .ZN(n72) );
  AOI22D1BWP U100 ( .A1(n92), .A2(n68), .B1(n90), .B2(n78), .ZN(n71) );
  XNR2D1BWP U101 ( .A1(n69), .A2(io_b[1]), .ZN(n70) );
  MUX2ND1BWP U102 ( .I0(n72), .I1(n71), .S(n70), .ZN(n73) );
  OR2D8BWP U103 ( .A1(n74), .A2(n73), .Z(io_out[1]) );
  CKND1BWP U104 ( .I(io_b[0]), .ZN(n79) );
  OA21D1BWP U105 ( .A1(io_b[0]), .A2(io_opcode[1]), .B(n75), .Z(n76) );
  INVD12BWP U106 ( .I(n81), .ZN(io_out[0]) );
  AO22D1BWP U107 ( .A1(io_b[3]), .A2(n83), .B1(io_a[3]), .B2(n82), .Z(n95) );
  FCICOND1BWP U108 ( .A(n85), .B(n84), .CI(io_b[2]), .CON(n89) );
  CKND1BWP U109 ( .I(n89), .ZN(n88) );
  CKND1BWP U110 ( .I(n91), .ZN(n87) );
  AOI22D1BWP U111 ( .A1(n92), .A2(n91), .B1(n90), .B2(n89), .ZN(n93) );
  NR2D4BWP U112 ( .A1(n95), .A2(n94), .ZN(n96) );
  INVD16BWP U113 ( .I(n96), .ZN(io_out[3]) );
endmodule

