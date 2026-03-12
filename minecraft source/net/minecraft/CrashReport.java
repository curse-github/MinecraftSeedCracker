/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashReport
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  26 */   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT); private final String title; private final Throwable exception; private final List<CrashReportCategory> details; private Path saveFile; private boolean trackingStackTrace; private StackTraceElement[] uncategorizedStackTrace;
/*     */   private final SystemReport systemReport;
/*     */   
/*     */   public CrashReport(String title, Throwable t) {
/*  30 */     this.details = Lists.newArrayList();
/*     */     
/*  32 */     this.trackingStackTrace = true;
/*  33 */     this.uncategorizedStackTrace = new StackTraceElement[0];
/*     */     
/*  35 */     this.systemReport = new SystemReport();
/*     */ 
/*     */     
/*  38 */     this.title = title;
/*  39 */     this.exception = t;
/*     */   }
/*     */ 
/*     */   
/*  43 */   public String getTitle() { return this.title; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Throwable getException() { return this.exception; }
/*     */ 
/*     */   
/*     */   public String getDetails() {
/*  51 */     StringBuilder builder = new StringBuilder();
/*     */     
/*  53 */     getDetails(builder);
/*     */     
/*  55 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder builder) {
/*  59 */     if ((this.uncategorizedStackTrace == null || this.uncategorizedStackTrace.length <= 0) && !this.details.isEmpty()) {
/*  60 */       this.uncategorizedStackTrace = (StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.details.get(0)).getStacktrace(), 0, 1);
/*     */     }
/*     */     
/*  63 */     if (this.uncategorizedStackTrace != null && this.uncategorizedStackTrace.length > 0) {
/*  64 */       builder.append("-- Head --\n");
/*  65 */       builder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
/*  66 */       builder.append("Stacktrace:\n");
/*     */       
/*  68 */       for (StackTraceElement element : this.uncategorizedStackTrace) {
/*  69 */         builder.append("\t").append("at ").append(element);
/*  70 */         builder.append("\n");
/*     */       } 
/*  72 */       builder.append("\n");
/*     */     } 
/*     */     
/*  75 */     for (CrashReportCategory entry : this.details) {
/*  76 */       entry.getDetails(builder);
/*  77 */       builder.append("\n\n");
/*     */     } 
/*     */     
/*  80 */     this.systemReport.appendToCrashReportString(builder);
/*     */   }
/*     */   
/*     */   public String getExceptionMessage() {
/*  84 */     writer = null;
/*  85 */     printWriter = null;
/*  86 */     Throwable exception = this.exception;
/*     */     
/*  88 */     if (exception.getMessage() == null) {
/*     */       
/*  90 */       if (exception instanceof NullPointerException) {
/*  91 */         exception = new NullPointerException(this.title);
/*  92 */       } else if (exception instanceof StackOverflowError) {
/*  93 */         exception = new StackOverflowError(this.title);
/*  94 */       } else if (exception instanceof OutOfMemoryError) {
/*  95 */         exception = new OutOfMemoryError(this.title);
/*     */       } 
/*     */       
/*  98 */       exception.setStackTrace(this.exception.getStackTrace());
/*     */     } 
/*     */     
/*     */     try {
/* 102 */       writer = new StringWriter();
/* 103 */       printWriter = new PrintWriter(writer);
/* 104 */       exception.printStackTrace(printWriter);
/* 105 */       return writer.toString();
/*     */     } finally {
/* 107 */       IOUtils.closeQuietly(writer);
/* 108 */       IOUtils.closeQuietly(printWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFriendlyReport(ReportType reportType, List<String> extraComments) {
/* 113 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 115 */     reportType.appendHeader(builder, extraComments);
/*     */     
/* 117 */     builder.append("Time: ");
/* 118 */     builder.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
/* 119 */     builder.append("\n");
/*     */     
/* 121 */     builder.append("Description: ");
/* 122 */     builder.append(this.title);
/* 123 */     builder.append("\n\n");
/*     */     
/* 125 */     builder.append(getExceptionMessage());
/* 126 */     builder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/*     */     
/* 128 */     for (int i = 0; i < 87; i++) {
/* 129 */       builder.append("-");
/*     */     }
/* 131 */     builder.append("\n\n");
/* 132 */     getDetails(builder);
/*     */     
/* 134 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/* 138 */   public String getFriendlyReport(ReportType reportType) { return getFriendlyReport(reportType, List.of()); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public Path getSaveFile() { return this.saveFile; }
/*     */ 
/*     */   
/*     */   public boolean saveToFile(Path saveFile, ReportType reportType, List<String> extraComments) {
/* 146 */     if (this.saveFile != null) {
/* 147 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 151 */       if (saveFile.getParent() != null) {
/* 152 */         FileUtil.createDirectoriesSafe(saveFile.getParent());
/*     */       }
/*     */       
/* 155 */       Writer writer = Files.newBufferedWriter(saveFile, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 156 */       try { writer.write(getFriendlyReport(reportType, extraComments));
/* 157 */         if (writer != null) writer.close();  } catch (Throwable throwable) { if (writer != null)
/*     */           try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 159 */        this.saveFile = saveFile;
/* 160 */       return true;
/* 161 */     } catch (Throwable t) {
/* 162 */       LOGGER.error("Could not save crash report to {}", saveFile, t);
/* 163 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 168 */   public boolean saveToFile(Path file, ReportType reportType) { return saveToFile(file, reportType, List.of()); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public SystemReport getSystemReport() { return this.systemReport; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public CrashReportCategory addCategory(String name) { return addCategory(name, 1); }
/*     */ 
/*     */   
/*     */   public CrashReportCategory addCategory(String name, int nestedOffset) {
/* 180 */     CrashReportCategory category = new CrashReportCategory(name);
/*     */     
/* 182 */     if (this.trackingStackTrace) {
/* 183 */       int size = category.fillInStackTrace(nestedOffset);
/* 184 */       StackTraceElement[] fullTrace = this.exception.getStackTrace();
/* 185 */       StackTraceElement source = null;
/* 186 */       StackTraceElement next = null;
/*     */       
/* 188 */       int traceIndex = fullTrace.length - size;
/* 189 */       if (traceIndex < 0) {
/* 190 */         LOGGER.error("Negative index in crash report handler ({}/{})", Integer.valueOf(fullTrace.length), Integer.valueOf(size));
/*     */       }
/*     */       
/* 193 */       if (fullTrace != null && 0 <= traceIndex && traceIndex < fullTrace.length) {
/* 194 */         source = fullTrace[traceIndex];
/*     */         
/* 196 */         if (fullTrace.length + 1 - size < fullTrace.length) {
/* 197 */           next = fullTrace[fullTrace.length + 1 - size];
/*     */         }
/*     */       } 
/*     */       
/* 201 */       this.trackingStackTrace = category.validateStackTrace(source, next);
/*     */       
/* 203 */       if (fullTrace != null && fullTrace.length >= size && 0 <= traceIndex && traceIndex < fullTrace.length) {
/* 204 */         this.uncategorizedStackTrace = new StackTraceElement[traceIndex];
/* 205 */         System.arraycopy(fullTrace, 0, this.uncategorizedStackTrace, 0, this.uncategorizedStackTrace.length);
/*     */       } else {
/* 207 */         this.trackingStackTrace = false;
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     this.details.add(category);
/* 212 */     return category;
/*     */   }
/*     */ 
/*     */   
/*     */   public static CrashReport forThrowable(Throwable t, String title) {
/*     */     CrashReport report;
/* 218 */     while (t instanceof java.util.concurrent.CompletionException && t.getCause() != null) {
/* 219 */       t = t.getCause();
/*     */     }
/*     */     
/* 222 */     if (t instanceof ReportedException) { ReportedException reportedException = (ReportedException)t;
/* 223 */       report = reportedException.getReport(); }
/*     */     else
/* 225 */     { report = new CrashReport(title, t); }
/*     */ 
/*     */     
/* 228 */     return report;
/*     */   }
/*     */   
/*     */   public static void preload() {
/* 232 */     MemoryReserve.allocate();
/* 233 */     (new CrashReport("Don't panic!", new Throwable())).getFriendlyReport(ReportType.CRASH);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\CrashReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */