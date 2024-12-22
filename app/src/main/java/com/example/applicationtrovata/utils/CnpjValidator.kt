package com.example.applicationtrovata.utils

class CnpjValidator {
    companion object {
        fun isValid(cnpj: String): Boolean {
            // Remove any non-digit characters
            val cleanCnpj = cnpj.replace(Regex("[^0-9]"), "")

            // Check if it has 14 digits
            if (cleanCnpj.length != 14) return false

            // Check if all digits are the same
            if (cleanCnpj.all { it == cleanCnpj[0] }) return false

            // Calculate first verification digit
            val digit1 = calculateVerificationDigit(cleanCnpj.substring(0, 12))
            if (digit1 != cleanCnpj[12].toString().toInt()) return false

            // Calculate second verification digit
            val digit2 = calculateVerificationDigit(cleanCnpj.substring(0, 13))
            if (digit2 != cleanCnpj[13].toString().toInt()) return false

            return true
        }

        private fun calculateVerificationDigit(base: String): Int {
            var factor = if (base.length == 12) 5 else 6
            var sum = 0

            // Calculate sum using the multiplication factors
            for (digit in base) {
                sum += digit.toString().toInt() * factor
                factor = if (factor == 2) 9 else factor - 1
            }

            // Calculate verification digit
            val remainder = sum % 11
            return if (remainder < 2) 0 else 11 - remainder
        }

        fun formatCnpj(cnpj: String): String {
            val cleanCnpj = cnpj.replace(Regex("[^0-9]"), "")
            if (cleanCnpj.length != 14) return cleanCnpj

            return "${cleanCnpj.substring(0,2)}.${cleanCnpj.substring(2,5)}." +
                    "${cleanCnpj.substring(5,8)}/${cleanCnpj.substring(8,12)}-${cleanCnpj.substring(12)}"
        }
    }
}