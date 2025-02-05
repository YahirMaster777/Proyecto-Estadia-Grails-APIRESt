package com.ordenaris.internalControl

class SectionProfile {

    Profile profile
    Section section

    static mapping = {
        version false
        permission sqlType: "int(11)"
    }
    static constraints = {
        permission nullable: false, blank: false
    }
}
