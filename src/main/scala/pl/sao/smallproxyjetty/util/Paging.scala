package pl.sao.smallproxyjetty.util

class Paging(val overListSize: Int = 1) {

  def whileNextRequest[T <: Any](cond : => Boolean)(block : => Unit): Seq[T] = {
    if(cond) {
      block
      whileNextRequest(cond)(block)
    } else {
      Seq.empty[T]
    }
  }
  var urlsBlock: Boolean = false
  var lastPages: IndexedSeq[scala.collection.mutable.Map[Int,Int]] = for {
    i <- 0 to overListSize
  } yield {
    scala.collection.mutable.Map.empty[Int,Int].withDefaultValue(0)
  }

  def getNextItemWithPage(collector: Int) = {
    Thread.sleep(10 * collector)
    while ( urlsBlock ) { Thread.sleep(collector) }
    var found = false
    var nextItem = 0
    var nextPage = 1
    while(!found) {
      val doneByCollector = lastPages(nextItem)(nextPage)
      if (doneByCollector == 0 )
        found = true
      else
        if ( nextItem >= overListSize) {
          nextPage += 1
          nextItem = 0
        } else {
          nextItem += 1
        }
    }
    (nextItem, nextPage)
  }
  def saveLastItemPage(collector: Int, item: Int, page: Int, savePage: Int): Unit = {
    while (urlsBlock) { Thread.sleep(collector) }
    urlsBlock = true
    lastPages(item)(page) = savePage
    urlsBlock = false
  }
}
