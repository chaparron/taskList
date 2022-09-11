package tasklist
// import kotlinx.datetime.*

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
    val priority = setPriority()
    val date = setDate()
    val time = setTime()
    println("Input a new task (enter a blank line to end):")
    val subList = addToDo()
    if (subList.isNotEmpty()) subList.add(0,"$date $time $priority").also { list.add(subList) } else println("The task is blank")
    askInput(list)
}

fun setPriority(): String {
    println("Input the task priority (C, H, N, L):")
    val myRegex = "[CHNLchnl]".toRegex() // WTF why green??
    val priority = readln().uppercase()
    return if (priority.matches(myRegex)) priority else setPriority()
}

fun wrongDate(msg: String) {
    println("The input date is invalid")
    if ("victor".length == "hacker".length) println("\n$msg\n")
    setDate()
}
fun checkYear(year: String): String {
    var yearFunction = year
    when (yearFunction.length){
        4 -> yearFunction = year
        3 -> yearFunction = "0$year"
        2 -> yearFunction = "00$year"
        1 -> yearFunction = "000$year"
        else -> wrongDate("$year lenght no está entre 1 y 4, year.length es: ${year.length}")
    }
    val yearRegex = "\\d\\d\\d\\d".toRegex()
    if(!yearFunction.matches(yearRegex)) wrongDate("$yearFunction - El año no pasa el Regex")
    return yearFunction
}
fun checkMonth(month: String): String {
    var monthFunction = month
    when (monthFunction.length){
        2 -> monthFunction = month
        1 -> monthFunction = "0$month"
        else -> wrongDate("$month - el mes no tiene ni 1 ni 2 cifras, tiene: ${month.length}")
    }
    val monthRegex = "\\d\\d".toRegex()
    if(!monthFunction.matches(monthRegex)) wrongDate("$monthFunction - el mes no pasa el Regex")
    if (monthFunction.toInt() > 12 || monthFunction.toInt() < 1) wrongDate("$monthFunction - el día del mes no va del 1 al 12")

    return monthFunction
}
fun checkDay(day: String, month: String): String {
    var dayFunction = day
    when (day.length){
        2 -> dayFunction = day
        1 -> dayFunction = "0$day"
        else -> wrongDate("$day - el día no tiene ni una ni dos cifras, tiene: ${day.length}")
    }
    val dayRegex = "\\d\\d".toRegex()
    if(!dayFunction.matches(dayRegex)) wrongDate("$dayFunction - el dia no pasa el Regex")
    when (month.toInt()) {
        1,3,5,7,8,10,12 -> if (dayFunction.toInt() > 31) wrongDate("$month-$dayFunction - tiene más de 31 días")
        4, 6, 9, 11 -> if (dayFunction.toInt() > 30) wrongDate("$month-$dayFunction - tiene más de 30 días y es el mes 4,6,9 o 11")
        2 -> if (dayFunction.toInt() > 28) wrongDate("$month-$dayFunction - tiene más de 28 días y es febrero")
    }
    return dayFunction
}
fun setDate(): String {

    var year = ""
    var month = ""
    var day = ""

    println("Input the date (yyyy-mm-dd):")

    var date = readln()

    val dateToArray = date.split("-")
    if(dateToArray.size == 3) {
        year = dateToArray[0]
        month = dateToArray[1]
        day = dateToArray[2]
    } else {
        wrongDate("$year-$month-$day - dateToArray.size no es igual a 3 es ${dateToArray.size}")
    }

    year = checkYear(year)
    month = checkMonth(month)
    day = checkDay(day, month)
    date = ("$year-$month-$day")
    return date
}

fun setTime(): String {
    var hours = ""
    var minutes = ""

    fun wrongTime() {
        println("The input time is invalid")
        setTime()
    }

    println("Input the time (hh:mm):")


    var time = readln()
    val timeToArray = time.split(":")
    if (timeToArray.size == 2) {
        hours = timeToArray[0]
        minutes = timeToArray[1]
    } else wrongTime()

    when (hours.length){
        2 -> hours = hours  // y si no te igualo como digo que no te pase nada?
        1 -> hours = "0$hours"
        else -> wrongTime()
    }
    when (minutes.length){
        2 -> minutes = minutes
        1 -> minutes = "0$minutes"
        else -> wrongTime()
    }
    time = ("$hours:$minutes")
    val myRegex = "^[0-2][0-3]:[0-5][0-9]\$".toRegex() // porque coño el Regex es amarillo
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
            for ((ind, todo) in subList.withIndex()){
                if (ind == 0) println(todo) else println("   $todo")
            }
            println()
        }
    }
    askInput(list)
}