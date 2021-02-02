package pl.sao.smallproxyjetty.util

case class ResponseData[T](status: Int, code: String, data: List[T])
