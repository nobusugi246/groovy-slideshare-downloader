@Grab('org.jsoup:jsoup:1.9.2')
import org.jsoup.Jsoup

final cwjp = 'http://crazyworks.jp/slideshare_downloder/'

final targetFile = args[0]
final targetFolder = args[1]

final proxyHost
final proxyPort
try {
  proxyHost = args[2]
  proxyPort = args[3]
} catch (e) {
}
  
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

    def conn = Jsoup.connect("${cwjp}?url=${url}").timeout(600000)
    if( proxyPort && proxyPort ){
      conn.proxy(proxyHost as String, proxyPort as int)
    }
    doc = conn.get()
    def pdf = doc.select('a')[2].attr('href') as String

    new File("${targetFolder}/${name}.pdf") << new URL("${cwjp}${pdf}").bytes
    println 'done'
  } catch (e) {
    println e
    try {
      println doc.select('h2')[-1]
    } catch (f) {
    }

    new File("${targetFile}.error").append "${url}¥n"
  }
}
