package org.easyit.demo.api;

public interface CutPoint {


    String getName();

    String getType();

    String getClassName();

    String getMethodName();


    class CutPointImpl implements CutPoint {
        private String name;
        private String type;
        private String className;
        private String methodName;

        public CutPointImpl(String name, String type, String className, String methodName) {
            this.name = name;
            this.type = type;
            this.className = className;
            this.methodName = methodName;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }
    }
}
