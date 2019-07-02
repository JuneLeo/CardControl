package com.card.complier;

import com.card.annotation.CardMap;
import com.card.generator.IGenerator;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class CardProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(CardMap.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CardMap.class);

        try {
            TypeSpec type = getRouterTableInitializer(elements);
            if (type != null) {
                JavaFile.builder("com.card.generator", type).build().writeTo(mFiler);
            }
        } catch (FilerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            error(e.getMessage());
        }
        return true;
    }

    private TypeSpec getRouterTableInitializer(Set<? extends Element> elements) throws ClassNotFoundException {
        if (elements == null || elements.size() == 0) {
            return null;
        }

        ParameterizedTypeName cardNameListTypeName = ParameterizedTypeName
                .get(ClassName.get(List.class), ClassName.get(String.class));

        ParameterizedTypeName providerNameListTypeName = ParameterizedTypeName
                .get(ClassName.get(List.class), ClassName.get(String.class));


        ParameterSpec cardNameListParameterSpec = ParameterSpec.builder(cardNameListTypeName, "cardNameList")
                .build();

        ParameterSpec providerNameListParameterSpec = ParameterSpec.builder(providerNameListTypeName, "providerNameList")
                .build();

        MethodSpec.Builder routerInitBuilder = MethodSpec.methodBuilder("initCardTable")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(cardNameListParameterSpec)
                .addParameter(providerNameListParameterSpec);

        for (Element element : elements) {
            //CardMap router = element.getAnnotation(CardMap.class);
            AnnotationMirror annotationMirror = getAnnotationMirror(element, CardMap.class);
            if (annotationMirror == null) {
                continue;
            }
            String[] arr = annotationMirror.toString().split("\\u0028");// )
            String s = arr[1];
            //routerInitBuilder.addStatement("router.put($S, $T.class)", s.substring(0, s.lastIndexOf(".")), ClassName.get((TypeElement) element));
            routerInitBuilder.addStatement("cardNameList.add($S)", s.substring(0, s.lastIndexOf(".")));
            ClassName clazz = ClassName.get((TypeElement) element);
            routerInitBuilder.addStatement("providerNameList.add($S)", clazz.packageName() + "." + clazz.simpleName());
        }

        MethodSpec routerInitMethod = routerInitBuilder.build();
        TypeElement routerInitializerType = elementUtils.getTypeElement(IGenerator.class.getCanonicalName());
        return TypeSpec.classBuilder("Generator")
                .addSuperinterface(ClassName.get(routerInitializerType))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(routerInitMethod)
                .build();
    }

    private static AnnotationMirror getAnnotationMirror(Element typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }

    private void error(String error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error);
    }

}
