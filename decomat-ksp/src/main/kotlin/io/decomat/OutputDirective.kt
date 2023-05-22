package io.decomat

import java.io.File
import java.io.FileWriter
import java.io.IOException
import freemarker.template.TemplateDirectiveModel
import freemarker.template.TemplateException
import freemarker.core.Environment
import freemarker.template.TemplateDirectiveBody
import freemarker.template.TemplateModel
import freemarker.template.SimpleScalar

class OutputDirective : TemplateDirectiveModel {
  @Throws(TemplateException::class, IOException::class)
  override fun execute(
    env: Environment,
    params: Map<*, *>,
    loopVars: Array<freemarker.template.TemplateModel>,
    body: TemplateDirectiveBody
  ) {
    val file: SimpleScalar? = params["file"] as SimpleScalar?
    val fw = FileWriter(File(file.toString()))
    body.render(fw)
    fw.flush()
  }
}

