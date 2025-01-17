package com.example.bookreadingapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class TableComposable(
    modifier: Modifier = Modifier
) {

    @Composable
    fun CreateTableComposable(tableString: String) {
        val parsedTableString = parseTableStringToList(tableString)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            parsedTableString.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(10.dp)
                ) {
                    row.forEach{ td ->
                        Text(text = td)
                    }
                }
            }
        }
    }

    private fun parseTableStringToList(tableString: String): List<List<String>> {
        val trSpecification = Regex("<tr.*?>(.*?)</tr>", RegexOption.DOT_MATCHES_ALL)
        val tdSpecification = Regex("<td.*?>(.*?)</td>")

        val tableRows = trSpecification.findAll(tableString)
        val returnList = tableRows.map { row ->
            tdSpecification.findAll(row.groupValues[1])
                .map { column ->
                    column.groupValues[1]
                        .replace(Regex("&nbsp"), " ")
                        .trim()
                }.toList()
        }.toList()

        return returnList
    }
}

