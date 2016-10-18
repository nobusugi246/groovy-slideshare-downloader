@Grab('org.jsoup:jsoup:1.9.2')
import org.jsoup.Jsoup

final cwjp = 'http://crazyworks.jp/slideshare_downloder/'

final targetFile = args[0]
final targetFolder = args[1]

new File(targetFolder).mkdirs()

def doc

int index = 0
new File(targetFile).eachLine { index++ }
final filesNum = index

index = 1
println "${targetFile} ========== ========== ========== ========== =========="
new File(targetFile).eachLine { url ->
  try {
    def name = url.split('/')[-1]
    print "${index++}/${filesNum}: ${name} ... "

    doc = Jsoup.connect("${cwjp}?url=${url}").timeout(600000).get()
    def pdf = doc.select('a')[2].attr('href') as String

    def ext = 2
    def pdfFileName = "${targetFolder}/${name}.pdf"
    while( new File(pdfFileName).exists() ){
      pdfFileName = "${targetFolder}/${name}_${ext++}.pdf"
    }
    new File(pdfFileName) << new URL("${cwjp}${pdf}").bytes
    println 'done'
  } catch (e) {
    println e
    try {
      println doc.select('h2')[-1]
    } catch (f) {
    }

    new File("${targetFile}.error").append "${url}${System.lineSeparator()}"
  }
}
