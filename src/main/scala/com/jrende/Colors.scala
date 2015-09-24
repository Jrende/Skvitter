package com.jrende

object Colors {
  val CLEAR = "\u001B[0m"
  val BLACK = "\u001B[30m"
  val RED = "\u001B[31m"
  val GREEN = "\u001B[32m"
  val YELLOW = "\u001B[33m"
  val BLUE = "\u001B[34m"
  val MAGENTA = "\u001B[35m"
  val CYAN = "\u001B[36m"
  val LIGHT_GRAY = "\u001B[37m"
  val DARK_GRAY = "\u001B[90m"
  val LIGHT_RED = "\u001B[91m"
  val LIGHT_GREEN = "\u001B[92m"
  val LIGHT_YELLOW = "\u001B[93m"
  val LIGHT_BLUE = "\u001B[94m"
  val LIGHT_MAGENTA = "\u001B[95m"
  val LIGHT_CYAN = "\u001B[96m"
  val WHITE = "\u001B[97m"

  val BOLD = "\u001B[1m"
  val DIM = "\u001B[2m"
  val UNDERLINE = "\u001B[4m"


  implicit class ColorString(msg: String) {
    def Black(): String = BLACK + msg + CLEAR
    def Red(): String = RED + msg + CLEAR
    def Green(): String = GREEN + msg + CLEAR
    def Yellow(): String = YELLOW + msg + CLEAR
    def Blue(): String = BLUE + msg + CLEAR
    def Magenta(): String = MAGENTA + msg + CLEAR
    def Cyan(): String = CYAN + msg + CLEAR
    def LightGray(): String = LIGHT_GRAY + msg + CLEAR
    def DarkGray(): String = DARK_GRAY + msg + CLEAR
    def LightRed(): String = LIGHT_RED + msg + CLEAR
    def LightGreen(): String = LIGHT_GREEN + msg + CLEAR
    def LightYellow(): String = LIGHT_YELLOW + msg + CLEAR
    def LightBlue(): String = LIGHT_BLUE + msg + CLEAR
    def LightMagenta(): String = LIGHT_MAGENTA + msg + CLEAR
    def LightCyan(): String = LIGHT_CYAN + msg + CLEAR
    def White(): String = WHITE + msg + CLEAR
    def Bold(): String = BOLD + msg + CLEAR
    def Dim(): String = DIM + msg + CLEAR
    def Underlined(): String = UNDERLINE + msg + CLEAR
  }
}


