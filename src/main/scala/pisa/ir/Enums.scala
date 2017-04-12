package plasticine.pisa.enums

import plasticine.pisa._

sealed trait Enum

sealed trait SelectSource extends Enum
object CounterSrc extends SelectSource
object ScalarFIFOSrc extends SelectSource
object VectorFIFOSrc extends SelectSource
object ConstSrc extends SelectSource
object XSrc extends SelectSource

sealed trait Opcode 
case object Mux extends Opcode
case object Bypass extends Opcode

sealed trait FixOpcode extends Opcode 
case object FixAdd extends FixOpcode 
case object FixSub extends FixOpcode 
case object FixMul extends FixOpcode 
case object FixDiv extends FixOpcode 
case object FixMin extends FixOpcode
case object FixMax extends FixOpcode 
case object FixLt  extends FixOpcode
case object FixLeq extends FixOpcode
case object FixEql extends FixOpcode
case object FixNeq extends FixOpcode
case object FixMod extends FixOpcode
case object FixSra extends FixOpcode
case object FixNeg extends FixOpcode

sealed trait FltOpcode extends Opcode 
case object FltAdd extends FltOpcode 
case object FltSub extends FltOpcode 
case object FltMul extends FltOpcode 
case object FltDiv extends FltOpcode 
case object FltMin extends FltOpcode 
case object FltMax extends FltOpcode 
case object FltLt  extends FltOpcode
case object FltLeq extends FltOpcode
case object FltEql extends FltOpcode
case object FltNeq extends FltOpcode
case object FltExp extends FltOpcode
case object FltAbs extends FltOpcode
case object FltLog extends FltOpcode
case object FltSqrt extends FltOpcode
case object FltNeg extends FltOpcode 

sealed trait BitOpcode extends Opcode 
case object BitAnd extends BitOpcode // &
case object BitOr  extends BitOpcode // |

case object XOp extends Opcode
