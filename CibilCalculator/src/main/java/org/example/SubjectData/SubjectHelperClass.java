package org.example.SubjectData;

import com.clearspring.analytics.util.Pair;
import net.jcip.annotations.Immutable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.example.ModelRegistry.Attribute;
import org.example.ModelData.IAttribute;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public  class SubjectHelperClass {

    // Helper method to extract class Name analogues to the attribute
    public static String getClassName(Attribute attribute) {
        char temp = attribute.getName().charAt(0);
        StringBuilder className = new StringBuilder("org.example.ModelData.");
        String attributeName = attribute.getName();
        className.append(attributeName.replace(attributeName.charAt(0), Character.toUpperCase(temp)));

        return className.toString();
    }

    //Helper method to check null checking in string wrt to model registry requirement
    public static boolean isNullAllowed(String attributeIndex, Attribute attribute) {

        // isBlank() used to avoid blank space as well as empty string
        return attribute.isNullable() || !attributeIndex.isBlank();
    }

    //Wrapper helper method to calculate cibil score
    public static IAttribute getCibilScore(String subjectClassPath, Attribute attribute, Subject subjectRecord)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        // helper class method will return fully qualified className
        Class<?> attributeClass = Class.forName(SubjectHelperClass.getClassName(attribute));
        Class<?> subjectClass = Class.forName(subjectClassPath);

        //parameter lLess ctor to handle null values
        Constructor<?> defaultConstructor = attributeClass.getConstructor();
        //calling specific ctor with int as only argument.
        Constructor<?> parametrisedConstructor = attributeClass.getConstructor(int.class);

        //to get methods dynamically as per the attribute name
        Method subjectClassMethod = subjectClass.getMethod("get" + attribute.getName());

        //storing method output in index
        String attributeIndex = (String) subjectClassMethod.invoke(subjectRecord);

        //Nullable checking
        if (SubjectHelperClass.isNullAllowed(attributeIndex, attribute)) {

            return getAttributeOutput(attributeIndex,attribute,defaultConstructor,parametrisedConstructor);

        } else {
            throw new RuntimeException("Null value not allowed for " + subjectRecord.getUserId() + " user in " + attribute.getName() + "attribute");
        }

    }

    private static IAttribute getAttributeOutput(String attributeIndex, Attribute attribute,
                                                 Constructor<?> defaultConstructor, Constructor<?> parametrisedConstructor)
            throws InvocationTargetException, InstantiationException, IllegalAccessException,IllegalArgumentException {

        try{
           if(!attributeIndex.isBlank())
           {
               return(IAttribute) parametrisedConstructor.newInstance(Integer.parseInt(attributeIndex));
           }
           // case where empty string is passed.
            return (IAttribute) defaultConstructor.newInstance();
        }
        catch(NullPointerException ex)
        {
            // if null value allowed assigning score 0 to that attribute.
            return (IAttribute) defaultConstructor.newInstance();
        }

    }

    public static List<OutputSubject> getCibilScoreList(List<Subject> subjects, List<Attribute> modelRegistry, String subjectClassPath) {




        // Initialised a list to store output data (CibilScore,UserId)
        List<OutputSubject> outputList = new ArrayList<>();

        subjects.forEach(subject -> {
            List<IAttribute> subjectsAttributeSet = new ArrayList<>();
            try {
                int cibilScore = 0;
                for (Attribute attribute : modelRegistry) {

                    IAttribute modelAttribute =  SubjectHelperClass.getCibilScore(subjectClassPath,attribute,subject);
//                    cibilScore += modelAttribute.getScore();
                    subjectsAttributeSet.add(modelAttribute);
                }

                outputList.add(new OutputSubject(subject.getUserId(),subjectsAttributeSet));

            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException ex) {
                ex.printStackTrace();
            }


        });
        outputList.forEach(System.out::println);
        return outputList;
    }


}
