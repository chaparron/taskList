package tasklist
import kotlinx.datetime.*

fun main() {
    val list = mutableListOf(mutableListOf<String>())
    list.clear()
    askInput(list)

}

fun askInput(list: MutableList<MutableList<String>>) {
    println("Input an action (add, print, end):")
    when (readln().lowercase()) {
        "add" -> addSubList(list)
        "print" -> displayTasks(list)
        "end" -> println("Tasklist exiting!")
        else -> println("The input action is invalid").also { askInput(list) }
    }
}

fun addSubList(list: MutableList<MutableList<String>>) {
    val priority = askPriority()
    val date = askDate()
    val time = askTime()
    println("Input a new task (enter a blank line to end):")
    val subList = addToDo()
    if (subList.isNotEmpty()) subList.add(0,"$date $time $priority").also { list.add(subList) } else println("The task is blank")
    askInput(list)
}

fun askPriority(): String {
    println("Input the task priority (C, H, N, L):")
    val myRegex = "[C/H/N/L/c/h/n/l]".toRegex()
    val priority = readln().toString().uppercase()
    return if (priority.matches(myRegex)) priority else askPriority()
}
fun wrongDate(msg: String) {
    println("The input date is invalid")
    if (true) println("\n$msg\n")
    askDate()
}

fun checkYear(year: String): String {
    var year = year
    when {
        year.length == 4 -> year = year
        year.length == 3 -> year = "0$year"
        year.length == 2 -> year = "00$year"
        year.length == 1 -> year = "000$year"
        else -> wrongDate("$year Year lenght no está entre 1 y 4, year.length es: ${year.length}")
    }
    val yearRegex = "\\d\\d\\d\\d".toRegex()
    if(!year.matches(yearRegex)) wrongDate("$year - El año no pasa el Regex")
    return year
}

fun checkMonth(month: String): String {
    var month = month
    when {
        month.length == 2 -> month = month
        month.length == 1 -> month = "0$month"
        else -> wrongDate("$month - el mes no tiene ni 1 ni 2 cifras, tiene: ${month.length}")
    }
    val monthRegex = "\\d\\d".toRegex()
    if(!month.matches(monthRegex)) wrongDate("$month - el mes no pasa el Regex")
    if (month.toInt() > 12 || month.toInt() < 1) wrongDate("$month - el día del mes no va del 1 al 12")

    return month
}

fun checkDay(day: String, month: String): String {
    var day = day
    when {
        day.length == 2 -> day = day
        day.length == 1 -> day = "0$day"
        else -> wrongDate("$day - el día no tiene ni una ni dos cifras, tiene: ${day.length}")
    }
    val dayRegex = "\\d\\d".toRegex()
    if(!day.matches(dayRegex)) wrongDate("$day - el dia no pasa el Regex")
    when (month.toInt()) {
        1,3,5,7,8,10,12 -> if (day.toInt() > 31) wrongDate("$month-$day - tiene más de 31 días")
        4, 6, 9, 11 -> if (day.toInt() > 30) wrongDate("$month-$day - tiene más de 30 días y es el mes 4,6,9 o 11")
        2 -> if (day.toInt() > 28) wrongDate("$month-$day - tiene más de 28 días y es febrero")
    }
    return day
}

fun askDate(): String {

    var year = ""
    var month = ""
    var day = ""
    var date = ""

    println("Input the date (yyyy-mm-dd):")

    date = readln().toString()

    val dateToArray = date.split("-")
    if(dateToArray.size == 3) {
        year = dateToArray[0].toString()
        month = dateToArray[1].toString()
        day = dateToArray[2].toString()
    } else {
        wrongDate("$year-$month-$day - dateToArray.size no es igual a 3 es ${dateToArray.size}")
    }

    year = checkYear(year)
    month = checkMonth(month)
    day = checkDay(day, month)
    date = ("$year-$month-$day")
    return date
}

fun askTime(): String {

    fun wrongTime() {
        println("The input time is invalid")
        askTime()
    }
    println("Input the time (hh:mm):")
    val myRegex = "^[0-2][0-3]:[0-5][0-9]\$".toRegex()
    var time = readln().toString()
    var hours = ""
    var minutes = ""
    var arrayed = time.split(":")
    if (arrayed.size == 2) {
        hours = arrayed[0]
        minutes = arrayed[1]
    } else wrongTime()
    when {
        hours.length == 2 -> hours = hours
        hours.length == 1 -> hours = "0$hours"
        else -> wrongTime()
    }
    when {
        minutes.length == 2 -> minutes = minutes
        minutes.length == 1 -> minutes = "0$minutes"
        else -> wrongTime()
    }
    time = ("$hours:$minutes")
    if (!time.matches(myRegex)) {
        wrongTime()
    }
    return time
}

fun addToDo(): MutableList<String> {
    val subList = mutableListOf<String>()
    while (true) {
        val todo = readln().trim()
        if (todo.isNotEmpty()) subList.add(todo) else break
    }
    return subList
}

fun displayTasks(list : MutableList<MutableList<String>>) {
    if (list.isEmpty()) {
        println("No tasks have been input")
    } else {
        for ((i,subList) in list.withIndex()) {
            val listNumber = i + 1
            print(listNumber.toString() + if (listNumber >= 10) " " else "  ")
            for ((i, todo) in subList.withIndex()){
                if (i == 0) println(todo) else println("   $todo")
            }
            println()
        }
    }
    askInput(list)
}