package org.easyit.demo.api;

public interface CutPoint {


    String getName();

    String getType();

    String getClassName();

    String getMethodName();

    String getEnhanceType();


    class CutPointImpl implements CutPoint {
        private String name;
        private String type;
        private String className;
        private String methodName;

        private String enhanceType;

        public CutPointImpl() {
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        @Override
        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public String getEnhanceType() {
            return enhanceType;
        }

        public void setEnhanceType(String enhanceType) {
            this.enhanceType = enhanceType;
        }
    }
}
