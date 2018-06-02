package xl.gcs.com.processor;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import xl.gcs.com.annotations.cls.BindView;

/**
 * 这里有用到annotations（Java Library），所以需要现在builder.gradle里面配置依赖下
 * 编译时注解处理器
 */

//Java 7以后，也可以用注解来代替下面的两个方法，但是考虑到兼容性的问题，这里不建议采用这种注解方式。
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes("xl.gcs.com.annotations.cls.BindView")

//谷歌开源的AutoService，用来帮助开发者生成META_INF/services/javax.annotation.processing.Processor
    //生成之后就可以被依赖使用了
    @AutoService(Processor.class)
public class ClassProcessor extends AbstractProcessor {

    //init: 被注解处理工具调用，并输入ProcessingEnvironment参数.ProcessingEnvironment提供很多有用的工具类，比如Elements,Types,Filer,Message等
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    //process: 相当于每个处理器的主函数main(),在这里写你的扫描，评估和处理注解的代码，以及生成的Java文件。输入参数RoundEnvironment可以让你查询出包含特定注解的被注解元素
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        //查询出包含特定注解的被注解元素
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                //Message的printMessage方法用来打印出注解修饰的成员变量的名称，这个名称会再AndroidStudio的Gradle Console窗口中打印出来
                messager.printMessage(Diagnostic.Kind.NOTE, "printMessage:" + element.toString());
            }
        }
        //该方法返回ture表示该注解已经被处理, 后续不会再有其他处理器处理; 返回false表示仍可被其他处理器处理.
        return true;
    }

    //getSupportedAnnotationTypes: 这是必须指定的方法，指定这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //LinkedHashSet集合同样是根据元素的hashCode值来决定元素的存储位置，但是它同时使用链表维护元素的次序。这样使得元素看起 来像是以插入顺序保存的，也就是说，当遍历该集合时候，LinkedHashSet将会以元素的添加顺序访问集合的元素。LinkedHashSet在迭代访问Set中的全部元素时，性能比HashSet好，但是插入时性能稍微逊色于HashSet。
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(BindView.class.getCanonicalName());
        return annotataions;
    }

    //用来指定你使用的Java版本，通常这里返回SourceVersion.latestSupported()
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}