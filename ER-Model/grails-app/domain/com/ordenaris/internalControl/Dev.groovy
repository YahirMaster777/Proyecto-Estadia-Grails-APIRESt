package com.ordenaris.internalControl

class Dev {
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    
    static constraints = {
    }
}
