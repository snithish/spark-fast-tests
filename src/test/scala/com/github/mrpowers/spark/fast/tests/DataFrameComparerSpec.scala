package com.github.mrpowers.spark.fast.tests

import org.scalatest.FunSpec

class DataFrameComparerSpec
    extends FunSpec
    with DataFrameComparer
    with SparkSessionTestWrapper {

  import spark.implicits._

  describe("#assertLargeDataFrameEquality") {

    it("does nothing true if the DataFrames have the same schemas and content") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (1),
        (5)
      ).toDF("number")

      assertLargeDataFrameEquality(sourceDF, expectedDF)

    }

    it("throws an error if the DataFrames have the different schemas") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (1, "word"),
        (5, "word")
      ).toDF("number", "word")

      intercept[DataFrameSchemaMismatch] {
        assertLargeDataFrameEquality(sourceDF, expectedDF)
      }

    }

    it("returns false if the DataFrames content is different") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (10),
        (5)
      ).toDF("number")

      intercept[org.scalatest.exceptions.TestFailedException] {
        assertLargeDataFrameEquality(sourceDF, expectedDF)
      }

    }

  }

  describe("#assertSmallDataFrameEquality") {

    it("does nothing true if the DataFrames have the same schemas and content") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (1),
        (5)
      ).toDF("number")

      assertSmallDataFrameEquality(sourceDF, expectedDF)

    }

    it("can performed unordered comparisons") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (5),
        (1)
      ).toDF("number")

      assertSmallDataFrameEquality(sourceDF, expectedDF, orderedComparison = false)

    }

    it("throws an error for unordered comparisons that don't match") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (5),
        (1),
        (10)
      ).toDF("number")

      intercept[DataFrameContentMismatch] {
        assertSmallDataFrameEquality(sourceDF, expectedDF, orderedComparison = false)
      }

    }

    it("throws an error if the DataFrames have the different schemas") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (1, "word"),
        (5, "word")
      ).toDF("number", "word")

      intercept[DataFrameSchemaMismatch] {
        assertSmallDataFrameEquality(sourceDF, expectedDF)
      }

    }

    it("returns false if the DataFrames content is different") {

      val sourceDF = Seq(
        (1),
        (5)
      ).toDF("number")

      val expectedDF = Seq(
        (10),
        (5)
      ).toDF("number")

      intercept[DataFrameContentMismatch] {
        assertSmallDataFrameEquality(sourceDF, expectedDF)
      }

    }

  }

  describe("#defaultSortDataFrame") {

    it("sorts a DataFrame by the column names in alphabetical order") {

      val sourceDF = Seq(
        (5, "bob"),
        (1, "phil"),
        (5, "anne")
      ).toDF("fun_level", "name")

      val actualDF = defaultSortDataFrame(sourceDF)

      val expectedDF = Seq(
        (1, "phil"),
        (5, "anne"),
        (5, "bob")
      ).toDF("fun_level", "name")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

}
