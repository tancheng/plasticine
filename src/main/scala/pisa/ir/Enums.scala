package plasticine.pisa.enums

import plasticine.pisa._

sealed trait Enum

sealed trait SelectType extends Enum
sealed trait XType extends SelectType
sealed trait SelectSource extends SelectType // Input type
sealed trait SelectDst extends SelectType // Ouptut type

object CounterSrc extends SelectSource
object ScalarFIFOSrc extends SelectSource
object VectorFIFOSrc extends SelectSource
object ConstSrc extends SelectSource
object PrevStageSrc extends SelectSource
object CurrStageSrc extends SelectSource
object ALUSrc extends SelectSource
object XSrc extends SelectSource with XType

object CurrStageDst extends SelectDst
object ScalarOutDst extends SelectDst
object VectorOutDst extends SelectDst
object XDst extends SelectDst with XType


sealed trait Opcode 
case object MuxOp extends Opcode
case object BypassA extends Opcode
case object BypassB extends Opcode
case object BypassC extends Opcode

sealed trait FixOpcode extends Opcode 
case object FixAdd extends FixOpcode 
case object FixSub extends FixOpcode 
case object FixMul extends FixOpcode 
case object FixDiv extends FixOpcode 
case object FixSHL  extends FixOpcode
case object FixSHR  extends FixOpcode
case object FixAnd extends FixOpcode 
case object FixOr extends FixOpcode 
case object FixMin extends FixOpcode
case object FixMax extends FixOpcode 
case object FixGt  extends FixOpcode
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
case object FltGt  extends FltOpcode
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
