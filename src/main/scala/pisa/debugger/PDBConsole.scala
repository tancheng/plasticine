//package plasticine.pisa.debugger
//
//import scala.tools.nsc.Settings
//import scala.tools.nsc.interpreter.ILoop
//
//object PDBConsole extends App {
//
////  if (args.size != 1) {
////    println(s"args = $args")
////    println("Usage: pdb <pisa file>")
////    sys.exit(-1)
////  }
////  val pisaFile = args(0)
//
//  val settings = new Settings
//  settings.usejavacp.value = true
//  settings.deprecation.value = true
//
//  new sys.SystemProperties += (
//    "scala.repl.autoruncode" -> "pdb.init",
//    "scala.color" -> "true"
//  )
//
//  new SampleILoop().process(settings)
////  new SampleILoop().run(pisaFile, settings)
//}
//
//class SampleILoop extends ILoop {
//  override def printWelcome() {
//    // ASCII art generated from http://patorjk.com/software/taag
//    println(Console.YELLOW + s"""
//
//██████╗ ██╗      █████╗ ███████╗████████╗██╗ ██████╗██╗███╗   ██╗███████╗
//██╔══██╗██║     ██╔══██╗██╔════╝╚══██╔══╝██║██╔════╝██║████╗  ██║██╔════╝
//██████╔╝██║     ███████║███████╗   ██║   ██║██║     ██║██╔██╗ ██║█████╗  
//██╔═══╝ ██║     ██╔══██║╚════██║   ██║   ██║██║     ██║██║╚██╗██║██╔══╝  
//██║     ███████╗██║  ██║███████║   ██║   ██║╚██████╗██║██║ ╚████║███████╗
//╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚═╝ ╚═════╝╚═╝╚═╝  ╚═══╝╚══════╝
//""" + Console.RESET)
//
//	println(Console.RED + s"""
//			██████╗ ███████╗██████╗ ██╗   ██╗ ██████╗  ██████╗ ███████╗██████╗ 
//			██╔══██╗██╔════╝██╔══██╗██║   ██║██╔════╝ ██╔════╝ ██╔════╝██╔══██╗
//			██║  ██║█████╗  ██████╔╝██║   ██║██║  ███╗██║  ███╗█████╗  ██████╔╝
//			██║  ██║██╔══╝  ██╔══██╗██║   ██║██║   ██║██║   ██║██╔══╝  ██╔══██╗
//			██████╔╝███████╗██████╔╝╚██████╔╝╚██████╔╝╚██████╔╝███████╗██║  ██║
//			╚═════╝ ╚══════╝╚═════╝  ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝
//""" + Console.RESET)
//  }
//
//  override def prompt = Console.GREEN + Console.BOLD + "(pdb) " + Console.RESET
//
//  def run(pisaFile: String, settings: Settings) = {
//    process(settings)
//  }
//}
