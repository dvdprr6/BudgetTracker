package com.api.service

import com.api.models.{CategoryGroupByWithTotalsDto, CategoryGroupByWithTotalsEntity, PostgresConnectionDto}
import com.api.repository.{CategoryRepository, CategoryRepositoryImpl}
import com.api.utils.Utils
import zio.{Task, ZIO, ZLayer}

trait CategoryService{
  def getCategoryGroupByWithTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsDto]]
}

class CategoryServiceImpl extends CategoryService {

  override def getCategoryGroupByWithTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsDto]] = {
    val categoryGroupByWithTotalsDtoRecords =
      for{
        categoryRepository <- ZIO.service[CategoryRepository]
        categoryGroupByWithTotalsEntity <- categoryRepository.getWithGroupByTotals()
        categoryGroupByWithTotalsDto = categoryGroupByWithTotalsEntity.map(record => toCategoryGroupByWithTotalsDto(record))
      }  yield categoryGroupByWithTotalsDto

    categoryGroupByWithTotalsDtoRecords.provide(CategoryRepositoryImpl.live)
  }

  private def toCategoryGroupByWithTotalsDto(categoryGroupByWithTotalsEntity: CategoryGroupByWithTotalsEntity): CategoryGroupByWithTotalsDto = {
    val id = categoryGroupByWithTotalsEntity.id
    val categoryName = categoryGroupByWithTotalsEntity.categoryName
    val total = categoryGroupByWithTotalsEntity.total
    val createDate = Utils.localDateTimeToString(categoryGroupByWithTotalsEntity.createDate)
    val modifiedDate = Utils.localDateTimeToString(categoryGroupByWithTotalsEntity.modifiedDate)

    CategoryGroupByWithTotalsDto(id, categoryName, total, createDate, modifiedDate)
  }
}

object CategoryServiceImpl{
  private def apply = new CategoryServiceImpl

  lazy val live: ZLayer[Any, Throwable, CategoryService] =
    ZLayer.succeed(apply)
}