/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import java.io.File;
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class JUnitLikeTestReporter
/*    */   implements TestReporter {
/*    */   private final Document document;
/*    */   private final Element testSuite;
/*    */   private final Stopwatch stopwatch;
/*    */   private final File destination;
/*    */   
/*    */   public JUnitLikeTestReporter(File destination) throws ParserConfigurationException {
/* 26 */     this.destination = destination;
/* 27 */     this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/* 28 */     this.testSuite = this.document.createElement("testsuite");
/* 29 */     Element testSuites = this.document.createElement("testsuite");
/* 30 */     testSuites.appendChild(this.testSuite);
/* 31 */     this.document.appendChild(testSuites);
/*    */     
/* 33 */     this.testSuite.setAttribute("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
/*    */     
/* 35 */     this.stopwatch = Stopwatch.createStarted();
/*    */   }
/*    */   
/*    */   private Element createTestCase(GameTestInfo testInfo, String name) {
/* 39 */     Element testCase = this.document.createElement("testcase");
/* 40 */     testCase.setAttribute("name", name);
/* 41 */     testCase.setAttribute("classname", testInfo.getStructure().toString());
/* 42 */     testCase.setAttribute("time", String.valueOf(testInfo.getRunTime() / 1000.0D));
/* 43 */     this.testSuite.appendChild(testCase);
/* 44 */     return testCase;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTestFailed(GameTestInfo testInfo) {
/* 49 */     String name = testInfo.id().toString();
/* 50 */     String message = testInfo.getError().getMessage();
/*    */     
/* 52 */     Element result = this.document.createElement(testInfo.isRequired() ? "failure" : "skipped");
/* 53 */     result.setAttribute("message", "(" + testInfo.getTestBlockPos().toShortString() + ") " + message);
/*    */     
/* 55 */     Element testCase = createTestCase(testInfo, name);
/* 56 */     testCase.appendChild(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTestSuccess(GameTestInfo testInfo) {
/* 61 */     String name = testInfo.id().toString();
/* 62 */     createTestCase(testInfo, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish() {
/* 67 */     this.stopwatch.stop();
/* 68 */     this.testSuite.setAttribute("time", String.valueOf(this.stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0D));
/*    */     
/*    */     try {
/* 71 */       save(this.destination);
/* 72 */     } catch (TransformerException exception) {
/* 73 */       throw new Error("Couldn't save test report", exception);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void save(File file) throws ParserConfigurationException {
/* 78 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 79 */     Transformer transformer = transformerFactory.newTransformer();
/* 80 */     DOMSource source = new DOMSource(this.document);
/* 81 */     StreamResult result = new StreamResult(file);
/* 82 */     transformer.transform(source, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\JUnitLikeTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */