package com.example.bookreadingapp.data

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException


class HtmlParser {

    private var doc: Document? = null

    @Throws(IOException::class)
    fun HtmlParser(path: String){
        this.doc = getDocument(path)
    }

    fun getDocument(path: String): Document{
        val source = File(path)
        val d = Jsoup.parse(source)
        return d
    }

    fun getChapters(): Elements{
        val body: Element = this.doc!!.body()
        return body.getElementsByClass("chapter")
    }

    fun getTitle(): String{
        val body: Element = this.doc!!.body()
        return body.getElementsByTag("h1").first().text()
    }

    fun getChapterTitle(chapter: Element): String{
        return chapter.child(0).text()
    }

    fun createChaptersMap(): Map<String, Element> {
        val chapters = this.getChapters()
        var chaptersMap = chapters.associateBy { getChapterTitle(it) }
        chaptersMap = chaptersMap.toMutableMap()
        val chaptersMapPlainText = chaptersMap.mapValues { it.value }
        return chaptersMapPlainText
    }

        //Note, map will be changed to <Object, String> once the objects are complete
    fun getChapterContents(chapter: Element): List<Pair<String, String>> {
        val content: Elements = chapter.children()

        val mappedContent : List<Pair<String, String>> = content.map{
            if (it.select("img").size == 1){
                Pair("image", it.select("img").attr("src"))
            }
            else if (it.`is`("p")){
                Pair("paragraph", it.text())
            }
            else if (it.`is`("h2, h3, h4, h5")){
                Pair("heading", it.text())
            }
            else if (it.`is`("table")){
                Pair("table", it.toString())
            }
            else{
                Pair("unknown", "")
            }
        }

        return mappedContent
    }

    @Preview(showBackground = true)
    @Composable
    fun Miserables(){
        PreviewTest("C:\\Users\\2236219\\Downloads\\bookreadingapp\\app\\src\\main\\java\\com\\example\\bookreadingapp\\data\\books\\pg135-images.html")
    }

    @Preview(showBackground = true)
    @Composable
    fun JungleBook(){
        PreviewTest("C:\\Users\\2236219\\Downloads\\bookreadingapp\\app\\src\\main\\java\\com\\example\\bookreadingapp\\data\\books\\pg236-images.html")
    }

    @Composable
    fun PreviewTest(path: String){
        //TODO: make this work with relative path
        this.HtmlParser(path)
//        this.getChapterTitle(this.getChapters()[9]).let { Text(it) }
//        this.getChapters().let { Text(it.toString()) }
//        this.createChaptersMap().let { Text(it.toString()) }
//        this.getChapterTitle(this.getChapters()[0]).let { Text(it.toString())}
//        this.getChapters().let { Text(it[4].children()[0].toString()) }
        this.getChapterContents(this.getChapters()[5]).let{ Text(it[27].toString()) }
    }

}