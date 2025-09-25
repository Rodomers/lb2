import java.util.Scanner
import kotlin.system.exitProcess

val scan = Scanner(System.`in`)

fun clearConsole(fake: Boolean = true) {
    if (!fake) {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }
    else {
        for (i in 0..50) {
            println()
        }
    }
}

fun addWorkMenu() {
    clearConsole()
    while (true) {
        println("Введите название работы или 0 для выхода")

        val line = scan.nextLine()
        if (line != "0") {
            val name = line.trim()
            var feeLevel: String
            do {
                println("Выберите уровень оплаты\n1. Низкий\n2. Средний\n3. Высокий")
                feeLevel = scan.nextLine()
            } while (feeLevel.trim() !in listOf("1", "2", "3"))

            when (feeLevel.toInt()) {
                3 -> PayrollDepartment.addWorkType(
                    Work(name, WorkFee.HIGH)
                )
                2 -> PayrollDepartment.addWorkType(
                    Work(name, WorkFee.MIDDLE)
                )
                else -> PayrollDepartment.addWorkType(
                    Work(name, WorkFee.LOW)
                )
            }
        }

//        println(PayrollDepartment.printWorkTypes())

        mainMenu()
        break
    }
}

fun employeeMenu(
    del: Boolean = false,
    addWork: Boolean = false,
    countFee: Boolean = false
    ) {
    val additionalStr = when {
        del -> "Вы выбрали удаление работника.\nСписок работников предприятия:\n${PayrollDepartment.printEmployees()}."
        addWork -> "Список работников предприятия:\n${PayrollDepartment.printEmployees()}.\nКем была выполнена работа?"
        countFee -> "Вы выбрали подсчёт выплаты работнику.\nСписок работников предприятия:\n${PayrollDepartment.printEmployees()}."
        else -> "Вы выбрали добавление нового работника."
    }

    clearConsole()
    while (true) {
        println("$additionalStr\nВведите фамилию работника или 0 для выхода")

        val line = scan.nextLine()
        var res: String? = null
        if (line != "0") {
            val name = line.trim()
            when {
                del -> {
                    res = if (!PayrollDepartment.deleteEmployee(name)) "Работник с такой фамилией не найден."
                    else "$name уволен(а)."
                }
                addWork -> {
                    if (!PayrollDepartment.checkEmployee(name)) res = "Такого работника не существует"
                    else {
                        println("Существующие в компании виды работ: ${PayrollDepartment.printWorkTypes()}\n" +
                                "Введите вид работы, который выполнил(а) $name.")
                        val type = scan.nextLine().trim()
                        if (!PayrollDepartment.addEmployeeDoneWork(name, type)) res = "Такого вида работ не существует."
                    }
                }
                countFee -> {
                    val fee = PayrollDepartment.countEmployeeFee(name)
                    res = when (fee) {
                        null -> "Такого работника не существует."
                        else -> "Сумма выплат для работника $name: $fee"
                    }
                }
                else -> {
                    if (!PayrollDepartment.addEmployee(Employee(name))) res = "Работник с такой фамилией уже существует."
                }
            }

        }

        mainMenu(text = res ?: "")
        break
    }
}

fun workTypesMenu() {
    clearConsole()
    println("Список видов работ, доступных на предприятии:\n${PayrollDepartment.printWorkTypes(full = true)}\n\n" +
            "Нажмите любую клавишу для возврата в главное меню")
    scan.nextLine()
    mainMenu()
}

fun mainMenu(text: String = "") {
    clearConsole()
    if (text != "") println(text)
    while (true) {
        println("Меню:")
        println("1. Добавить вид работы")
        println("2. Добавить работника")
        println("3. Удалить работника")
        println("4. Добавить выполненную работу")
        println("5. Посчитать зарплату работника")
        println("6. Посчитать зарплату всех работников")
        println("7. Получить список работников предприятия")
        println("8. Получить список видов работ")
        println("0. Выход из программы")

        val line = scan.nextLine()
        val intLine = try {
            line.toInt()
        } catch (e: Exception) {
            mainMenu()
        }

        when (intLine) {
            1 -> addWorkMenu()
            2 -> employeeMenu()
            3 -> employeeMenu(del = true)
            4 -> employeeMenu(addWork = true)
            5 -> employeeMenu(countFee = true)
            6 -> {
                mainMenu("Общая сумма выплат работников: ${PayrollDepartment.countFeesSum()}.")
            }
            7 -> mainMenu("Список работников предприятия: ${PayrollDepartment.printEmployees()}")
            8 -> workTypesMenu()
            else -> {
                clearConsole()
                println("Выход...")
                exitProcess(1)
            }
        }
    }
}

fun main() {
    mainMenu()
}