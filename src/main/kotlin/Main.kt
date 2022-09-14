import kotlinx.datetime.*
import com.squareup.moshi.*

enum class Priority(val humanStr: String, color: String) {
    C("Critical","\u001B[101m \u001B[0m"),
    H("High","\u001B[103m \u001B[0m"),
    N("Normal","\u001B[102m \u001B[0m"),
    L("Low","\u001B[104m \u001B[0m")
}

enum class Field {
    Priority, Date, Time, Task
}

class Task(var priority: Priority, var dateTime: LocalDateTime) {
    var list = mutableListOf<String>()
    fun add(s: String) {
        list.add(s)
    }
    fun dueTag(): String {
        val now = Clock.System.todayAt(TimeZone.UTC)
        val taskDate = dateTime.toString().split("T")[0].toLocalDate()
        return if (now > taskDate) {
            "\u001B[101m \u001B[0m" //"O" OutDated
        } else if (now == taskDate) {
            "\u001B[103m \u001B[0m" // "T" Today
        } else {
            "\u001B[102m \u001B[0m" // "I" In time
        }

    }
    fun printDateTime(): String {
        return dateTime.toString().split("T").joinToString(" | ")
    }

    fun getColor(): String{
        return when (priority.toString()) {
            "C" -> "\u001B[101m \u001B[0m"
            "H" -> "\u001B[103m \u001B[0m"
            "N" -> "\u001B[102m \u001B[0m"
            "L" -> "\u001B[104m \u001B[0m"
            else -> "you broke it"
        }
    }
    fun editPriority(newPriority: Priority) {
        priority = newPriority
    }
    fun editDate(year: Int, month: Int, day: Int) {
        dateTime = LocalDateTime(year, month, day, dateTime.hour, dateTime.minute)
    }
    fun editTime(hour: Int, minute: Int) {
        dateTime = LocalDateTime(dateTime.year, dateTime.monthNumber, dateTime.dayOfMonth, hour, minute)
    }
    fun editList( newList: MutableList<String>) {
        list = newList
    }
}

fun main() {
    val taskList = mutableListOf<Task>()

    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        val input = readln()
        when (input.lowercase()) {
            "add" -> addToList(taskList)
            "print" -> printList(taskList)
            "delete" -> deleteList(taskList)
            "edit" -> editList(taskList)
            "end" -> {
                println("Tasklist exiting!")
                break
            }

            else -> println("The input action is invalid")
        }
    }
}

private fun addToList(taskList: MutableList<Task>) {
    val priority = readPriority()
    val date = readDate()
    val time = readTime()

    val task = Task(priority, LocalDateTime(date.year, date.month, date.dayOfMonth, time.hour, time.minute))

    println("Input a new task (enter a blank line to end):")
    while (true) {
        val input = readln().trimIndent()
        if (input.isEmpty()) {
            break
        } else {
            task.add(input)
        }
    }

    if (task.list.isEmpty()) {
        println("The task is blank")
    } else {
        taskList.add(task)
    }
}

private fun readPriority(): Priority {
    while (true) {
        try {
            println("Input the task priority (C, H, N, L):")
            return Priority.valueOf(readln().uppercase())
        } catch (e: IllegalArgumentException) {
            // just try again without saying anything because jetbrains says so
        }
    }
}

private fun readDate(): LocalDate {
    while (true) try {
        println("Input the date (yyyy-mm-dd):")
        val (y, m, d) = readln().split("-").map { it.toInt() }
        return LocalDate(y, m, d)
    } catch (e: Exception) {
        println("The input date is invalid")
    }
}

private fun readTime(): LocalDateTime {
    while (true) try {
        println("Input the time (hh:mm):")
        val (h, m) = readln().split(":").map { it.toInt() }
        return LocalDateTime(2000, 1, 1, h, m)
    } catch (e: Exception) {
        println("The input time is invalid")
    }
}

private fun stillNoTasks () {
    println("No tasks have been input")
}
private fun printList(taskList: MutableList<Task>) {
    if (taskList.isEmpty()) {
        return stillNoTasks()
    }
    println("+----+------------+-------+---+---+--------------------------------------------+")
    println("| N  |    Date    | Time  | P | D |                   Task                     |")
    println("+----+------------+-------+---+---+--------------------------------------------+")
    taskList.forEachIndexed { index, task ->
        print(
            "| ${(index + 1).toString().padEnd(2)} | ${task.printDateTime()} | ${task.getColor()} | ${task.dueTag()} |"
        )
        for (i in 0 until task.list.size) {
            var arrayTask = task.list[i].toString().chunked(44)
            if (i == 0){
                for (ind in 0 until arrayTask.size){
                    if (ind == 0) {
                        print(arrayTask[0].padEnd(44,' ') + "|\n")
                    } else {
                        print("|    |            |       |   |   |${arrayTask[ind].padEnd(44,' ')}|\n")
                    }
                }
            } else {
                for (ind in 0 until arrayTask.size) {
                    print("|    |            |       |   |   |${arrayTask[ind].padEnd(44,' ')}|\n")
                }
            }
        }
        println("+----+------------+-------+---+---+--------------------------------------------+")
    }
    println()
}

private fun deleteList(taskList: MutableList<Task>) {
    if (taskList.size == 0) {
        return stillNoTasks()
    }
    printList(taskList)
    var selectedIndex: Int?
    outer@while (true) {
        println("Input the task number (1-${taskList.size}):")
        selectedIndex = readln().toIntOrNull()
        if (selectedIndex == null || selectedIndex > taskList.size || selectedIndex < 1) {
            println("Invalid task number")
            continue@outer
        } else break@outer
    }
    taskList.removeAt(selectedIndex!! - 1)
    println("The task is deleted")
}

private fun selectField():Field {
    while (true) try {
        println("Input a field to edit (priority, date, time, task):")
        return Field.valueOf(readln().replaceFirstChar{it.uppercase()})
    } catch (e: Exception) {
        println("Invalid field")
    }
}
private fun editList(taskList: MutableList<Task>) {
    if (taskList.size == 0) {
        return stillNoTasks()
    }
    printList(taskList)
    var selectedIndex: Int?
    outer@while (true) {
        println("Input the task number (1-${taskList.size}):")
        selectedIndex = readln().toIntOrNull()
        if (selectedIndex == null || selectedIndex > taskList.size || selectedIndex < 1) {
            println("Invalid task number")
            continue@outer
        }
        break@outer
    }
    val selectedTaskList = taskList[selectedIndex!! - 1]
    val field = selectField().toString()
    when (field) {
        "Priority" -> {
            val priority = readPriority()
            selectedTaskList.editPriority(priority)
        }
        "Date" -> {
            val date = readDate()
            selectedTaskList.editDate(date.year, date.monthNumber, date.dayOfMonth)
        }
        "Time" -> {
            val time = readTime()
            selectedTaskList.editTime(time.hour, time.minute)
        }
        "Task" -> {
//            val priority = taskList[selectedIndex!! - 1].priority
//            val dateTime = taskList[selectedIndex!! - 1].dateTime


            // fin del copia

            val task = mutableListOf<String>()

            println("Input a new task (enter a blank line to end):")
            while (true) {
                val input = readln().trimIndent()
                if (input.isEmpty()) {
                    break
                } else {
                    task.add(input)
                }
            }
            selectedTaskList.editList(task)

            // Fin del pega

        }
        else -> println("something went wrong")
    }
    println("The task is changed")
}