package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email = email, password = password)
        .also {
            user -> if (!map.containsKey(user.login)) map[user.login] = user
                else throw IllegalArgumentException("A user with this email already exists")
        }

    fun loginUser(login: String, password: String): String? {
        var userInfo: String? = null
        map[login.trim()]?.let {
            if (it.checkPassword(password)) userInfo = it.userInfo
            else null
        }
        var filteredPhone = login?.replace("""[^+\d]""".toRegex(),"")
        map[filteredPhone.trim()]?.let {
            if (it.checkPassword(password)) userInfo = it.userInfo
            else null
        }
        return userInfo
    }


    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        if(rawPhone?.replace("""[^+\d]""".toRegex(),"").length==12){
            return User.makeUser(fullName, phone = rawPhone).also {
                    user -> if (!map.containsKey(user.login)) map[user.login] = user
            else throw IllegalArgumentException("A user with this phone already exists")
            }
        } else throw java.lang.IllegalArgumentException("Phone number must contains 12 character")
    }


    fun requestAccessCode(phone: String) {
        var filteredPhone = phone?.replace("""[^+\d]""".toRegex(),"")
        if (map.containsKey(filteredPhone)) map[filteredPhone]?.updateAccessCode()
            else throw IllegalArgumentException("Request phone does not exist")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}