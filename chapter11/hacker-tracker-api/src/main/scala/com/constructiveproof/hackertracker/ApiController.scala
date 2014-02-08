package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.init.DatabaseSessionSupport
import com.constructiveproof.hackertracker.models.Hacker
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{BadRequest, MethodOverride, ScalatraServlet}
import org.scalatra.json._

class ApiController extends ScalatraServlet with MethodOverride
  with DatabaseSessionSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  /**
   * List all hackers.
   */
  get("/") {
    Hacker.all.toList
  }

  /**
   * Retrieve a specific hacker.
   */
  get("/:id") {
    val id = params.getAs[Int]("id").getOrElse(0)
    Hacker.get(id)
  }

  /**
   * Create a new hacker in the database.
   */
  post("/") {
    val firstName = params("firstname")
    val lastName = params("lastname")
    val motto = params("motto")
    val birthYear = params.getAs[Int]("birthyear").getOrElse(
      halt(BadRequest("Please provide a year of birth.")))

    val hacker = new Hacker(0, firstName, lastName, motto, birthYear)
    if(Hacker.create(hacker)) {
      hacker
    }
  }

  delete("/:id") {
    val id = params.getAs[Long]("id").getOrElse(
      halt(BadRequest("Please provide an id to destroy"))
    )
    Hacker.destroy(id)
  }

}
