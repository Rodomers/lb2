enum class WorkFee(val rate: Double) {
    HIGH(50.0),
    MIDDLE(40.0),
    LOW(35.0)
}

data class Work(val name: String, val fee: WorkFee)

class Employee(val surname: String) {
    val works = mutableListOf<Work>()

    fun addWork(work: Work) {
        works.add(work)
    }

    fun countFee(): Double {
        return works.sumOf { it.fee.rate }
    }
}

object PayrollDepartment {
    private val employees = mutableListOf<Employee>()
    private val workTypes = mutableListOf<Work>()

    private fun getEmployeeId(surname: String): Int {
        return employees.indexOfFirst { it.surname == surname }
    }

    private fun getWorkId(workName: String): Int {
        return workTypes.indexOfFirst { it.name == workName }
    }

    fun addEmployee(employee: Employee): Boolean {
        if (getEmployeeId(employee.surname) == -1) {
            employees.add(employee)
            return true
        }
        return false
    }

    fun deleteEmployee(surname: String): Boolean {
        val employeeId = getEmployeeId(surname)
        if (employeeId != -1) {
            employees.removeIf { it.surname == surname }
            return true
        }
        return false
    }

    fun addWorkType(work: Work): Boolean {
        if (getWorkId(work.name) == -1) {
            workTypes.add(work)
            return true
        }
        return false
    }

    fun addEmployeeDoneWork(surname: String, workName: String): Boolean {
        val employeeId = getEmployeeId(surname)
        val workId = getWorkId(workName)
        if (employeeId != -1 && workId != -1) {
            employees[employeeId].addWork(workTypes[workId])
            return true
        }
        return false
    }

    fun countEmployeeFee(surname: String): Double? {
        val employeeId = getEmployeeId(surname)

        if (employeeId != -1) {
            return employees[employeeId].countFee()
        }

        return null
    }

    fun countFeesSum(): Double {
        return employees.sumOf { it.countFee() }
    }

    fun printWorkTypes(full: Boolean = false): String {
        return when (full) {
            true -> workTypes.joinToString("\n") { it -> "${it.name}: ${it.fee} (${it.fee.rate})" }
            else -> workTypes.joinToString(", ") { it -> it.name }
        }
    }

    fun printEmployees(): String {
        return employees.joinToString(", ") { it -> it.surname }
    }

    fun checkEmployee(surname: String): Boolean {
        return getEmployeeId(surname) != -1
    }
}