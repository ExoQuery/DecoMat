package io.decomat

import java.io.OutputStreamWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import freemarker.template.TemplateDirectiveModel
import freemarker.template.TemplateException
import freemarker.core.Environment
import freemarker.template.Configuration
import freemarker.template.TemplateDirectiveBody
import freemarker.template.TemplateModel
import freemarker.template.SimpleScalar
import freemarker.template.Template
import java.io.Writer

object FreemarkerTest {
  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {
    val cfg = Configuration(Configuration.VERSION_2_3_0)
    cfg.setDefaultEncoding("UTF-8")

//    val template = Template(
//      "Test",
//      "<#assign model = model?eval_json><#list model.entities as entity><@output file=entity.name + \".txt\">This is \${entity.name} entity\n</@output></#list>",
//      cfg
//    )

    val template = Template("", File("/home/alexi/git/DecoMat/decomat-core/src/templates/Pattern3.ftl").readText(), cfg)

    val root =
      HashMap<String, Any>()
        .apply {
        put("output", OutputDirective())
        //put("model", model.encode())
      }

    val out: Writer = OutputStreamWriter(System.out)
    template.process(root, out)
  }
}