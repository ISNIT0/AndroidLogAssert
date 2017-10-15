# AndroidLogAssert
Log Assertion Library for use with Android Espresso tests

## What is this?
Logs are rarely tested. This libarary makes testing log statements slightly easier by dealing with the plumbing for you.
Creating a `new LogAssert()` will clear logcat (`logcat -c`) and return a class with 2 public methods:
- `assertLogsExist(String[] assertPatterns) throws Exception`

  *assertPatterns is an array of valid pattern strings (RegExp)*

  This method will throw an Exception if **any** of the assertPatterns is not matched
  
- `assertLogsCustom() throws Exception`

  This method will call an **overridden** method `customHandler` with a string of all logs, any thrown Exceptions will bubble up

## Example

### Basic
```java
@Test
public void clickLog100Button() throws Exception {
    ensureHasPermission("android.permission.READ_LOGS");
    LogAssert logAssert = new LogAssert(); // Clears logcat buffer (logcat -c)

    onView(withId(R.id.log100)).perform(click()); // UI Action / Trigger logs

    String[] assertArr = {"Logging 100 messages took [0-9]+ Milliseconds"};
    logAssert.assertLogsExist(assertArr); // Throws if it cannot find log messages matching all assertArr patterns
}
```

### Custom

```java
@Test
public void clickLog100Button() throws Exception {
    ensureHasPermission("android.permission.READ_LOGS");
    LogAssert logAssert = new LogAssert() { // Clears logcat buffer (logcat -c)
        @Override
        public void customHandler(String logs) throws Exception {
            throw new Exception("Not Implemented, logsString has length: [" + logs.length() + "]");
        }
    };

    onView(withId(R.id.log100)).perform(click()); // UI Action / Trigger logs

    logAssert.assertLogsCustom(); // Calls customHandler with android logs
}
```
