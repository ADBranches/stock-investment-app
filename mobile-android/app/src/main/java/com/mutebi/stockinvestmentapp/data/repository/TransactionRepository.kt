package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.domain.model.Transaction
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionApi: TransactionApi
) {
    suspend fun getTransactions(): Resource<List<Transaction>> {
        return try {
            val response = transactionApi.getTransactions()
            val body = response.body()
            val transactions = body?.data?.trades.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(transactions)
            } else {
                Resource.Error(body?.message ?: "Unable to load transactions")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load transactions")
        }
    }
}