package ru.yarsu.domain

import org.http4k.core.Uri

class Paginator(private val url: Uri, private val numberPage: Int, private val countPage: Int, private val category: String) {
    fun isPreviousPage(): Boolean{
        if (numberPage == 1) return false
        return true
    }
    fun isNextPage(): Boolean{
        if(numberPage == countPage) return false
        return true
    }

    fun getTwoPagesAroundCurrent(): List<String>{
        val listPages = mutableListOf<String>()
        if(isPreviousPage()){
            listPages.add((numberPage - 1).toString())
        }
        listPages.add("  ...  ")
        if(isNextPage()){
            listPages.add((numberPage + 1).toString())
        }
        return listPages
    }

    fun nextPage(): Uri{
        if(isNextPage()){
            var newUrl = url.path.substring(0, url.path.length - 1)
            newUrl += (numberPage + 1).toString()
            return url.path(newUrl)
        }
        return url
    }

    fun previousPage(): Uri{
        if(isNextPage()){
            var newUrl = url.path.substring(0, url.path.length - 1)
            newUrl += (numberPage - 1).toString()
            return url.path(newUrl)
        }
        return url
    }


}