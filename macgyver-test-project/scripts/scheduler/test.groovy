// #@Schedule {"cron":"*/5 * * * * ?", "enabled":false}

def val = Runtime.getRuntime().freeMemory() / (1024*1024)

//services['graphite'].record("freeMemory", val)



