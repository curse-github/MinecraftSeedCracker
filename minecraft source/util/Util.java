/*      */ package net.minecraft.util;
/*      */ 
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Iterators;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.util.concurrent.MoreExecutors;
/*      */ import com.mojang.datafixers.DSL;
/*      */ import com.mojang.datafixers.DataFixUtils;
/*      */ import com.mojang.datafixers.Typed;
/*      */ import com.mojang.datafixers.types.Type;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.jtracy.Zone;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectLists;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceImmutableList;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.spi.FileSystemProvider;
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.time.ZonedDateTime;
/*      */ import java.time.format.DateTimeFormatter;
/*      */ import java.time.format.FormatStyle;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ForkJoinPool;
/*      */ import java.util.concurrent.ForkJoinWorkerThread;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.LongStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CharPredicate;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SuppressForbidden;
/*      */ import net.minecraft.TracingExecutor;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.Bootstrap;
/*      */ import net.minecraft.util.datafix.DataFixers;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Util
/*      */ {
/*   94 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   private static final int DEFAULT_MAX_THREADS = 255;
/*      */   private static final int DEFAULT_SAFE_FILE_OPERATION_RETRIES = 10;
/*      */   private static final String MAX_THREADS_SYSTEM_PROPERTY = "max.bg.threads";
/*   98 */   private static final TracingExecutor BACKGROUND_EXECUTOR = makeExecutor("Main");
/*   99 */   private static final TracingExecutor IO_POOL = makeIoExecutor("IO-Worker-", false);
/*  100 */   private static final TracingExecutor DOWNLOAD_POOL = makeIoExecutor("Download-", true);
/*  101 */   private static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);
/*      */   public static final int LINEAR_LOOKUP_THRESHOLD = 8;
/*  103 */   private static final Set<String> ALLOWED_UNTRUSTED_LINK_PROTOCOLS = Set.of("http", "https");
/*      */   
/*      */   public static final long NANOS_PER_MILLI = 1000000L;
/*  106 */   public static TimeSource.NanoTimeSource timeSource = System::nanoTime;
/*  107 */   public static final Ticker TICKER = new Ticker()
/*      */     {
/*      */       public long read() {
/*  110 */         return Util.timeSource.getAsLong(); }
/*      */     };
/*      */   
/*  113 */   public static final UUID NIL_UUID = new UUID(0L, 0L);
/*  114 */   public static final FileSystemProvider ZIP_FILE_SYSTEM_PROVIDER = (FileSystemProvider)FileSystemProvider.installedProviders()
/*  115 */     .stream()
/*  116 */     .filter(p -> p.getScheme().equalsIgnoreCase("jar"))
/*  117 */     .findFirst()
/*  118 */     .orElseThrow(() -> new IllegalStateException("No jar file system provider found"));
/*      */   private static Consumer<String> thePauser = msg -> {
/*      */     
/*      */     };
/*      */   
/*  123 */   public static <K, V> Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() { return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue); }
/*      */ 
/*      */ 
/*      */   
/*  127 */   public static <T> Collector<T, ?, List<T>> toMutableList() { return Collectors.toCollection(Lists::newArrayList); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  132 */   public static <T extends Comparable<T>> String getPropertyName(Property<T> key, Object value) { return key.getName((Comparable)value); }
/*      */ 
/*      */   
/*      */   public static String makeDescriptionId(String prefix, Identifier location) {
/*  136 */     if (location == null) {
/*  137 */       return prefix + ".unregistered_sadface";
/*      */     }
/*  139 */     return prefix + "." + prefix + "." + location.getNamespace();
/*      */   }
/*      */ 
/*      */   
/*  143 */   public static long getMillis() { return getNanos() / 1000000L; }
/*      */ 
/*      */ 
/*      */   
/*  147 */   public static long getNanos() { return timeSource.getAsLong(); }
/*      */ 
/*      */ 
/*      */   
/*  151 */   public static long getEpochMillis() { return Instant.now().toEpochMilli(); }
/*      */ 
/*      */ 
/*      */   
/*  155 */   public static String getFilenameFormattedDateTime() { return FILENAME_DATE_TIME_FORMATTER.format(ZonedDateTime.now()); }
/*      */   
/*      */   private static TracingExecutor makeExecutor(String name) {
/*      */     ExecutorService executor;
/*  159 */     int threads = maxAllowedExecutorThreads();
/*      */     
/*  161 */     if (threads <= 0) {
/*      */       
/*  163 */       executor = MoreExecutors.newDirectExecutorService();
/*      */     } else {
/*  165 */       AtomicInteger workerCount = new AtomicInteger(1);
/*  166 */       executor = new ForkJoinPool(threads, pool -> {
/*  167 */             final String threadName = "Worker-" + name + "-" + workerCount.getAndIncrement();
/*  168 */             ForkJoinWorkerThread thread = new ForkJoinWorkerThread(pool)
/*      */               {
/*      */                 protected void onStart() {
/*  171 */                   TracyClient.setThreadName(threadName, name.hashCode());
/*  172 */                   super.onStart();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 protected void onTermination(Throwable exception) {
/*  177 */                   if (exception != null) {
/*  178 */                     Util.LOGGER.warn("{} died", getName(), exception);
/*      */                   } else {
/*  180 */                     Util.LOGGER.debug("{} shutdown", getName());
/*      */                   } 
/*  182 */                   super.onTermination(exception);
/*      */                 }
/*      */               };
/*  185 */             thread.setName(threadName);
/*  186 */             return thread;
/*      */           }Util::onThreadException, true);
/*      */     } 
/*  189 */     return new TracingExecutor(executor);
/*      */   }
/*      */ 
/*      */   
/*  193 */   public static int maxAllowedExecutorThreads() { return Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, getMaxThreads()); }
/*      */ 
/*      */   
/*      */   private static int getMaxThreads() {
/*  197 */     maxThreadsString = System.getProperty("max.bg.threads");
/*  198 */     if (maxThreadsString != null) {
/*      */       try {
/*  200 */         int maxThreads = Integer.parseInt(maxThreadsString);
/*  201 */         if (maxThreads >= 1 && maxThreads <= 255) {
/*  202 */           return maxThreads;
/*      */         }
/*  204 */         LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", new Object[] { "max.bg.threads", maxThreadsString, Integer.valueOf(255) });
/*  205 */       } catch (NumberFormatException e) {
/*  206 */         LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", new Object[] { "max.bg.threads", maxThreadsString, Integer.valueOf(255) });
/*      */       } 
/*      */     }
/*  209 */     return 255;
/*      */   }
/*      */ 
/*      */   
/*  213 */   public static TracingExecutor backgroundExecutor() { return BACKGROUND_EXECUTOR; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  220 */   public static TracingExecutor ioPool() { return IO_POOL; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  227 */   public static TracingExecutor nonCriticalIoPool() { return DOWNLOAD_POOL; }
/*      */ 
/*      */   
/*      */   public static void shutdownExecutors() {
/*  231 */     BACKGROUND_EXECUTOR.shutdownAndAwait(3L, TimeUnit.SECONDS);
/*  232 */     IO_POOL.shutdownAndAwait(3L, TimeUnit.SECONDS);
/*      */   }
/*      */   
/*      */   private static TracingExecutor makeIoExecutor(String prefix, boolean daemon) {
/*  236 */     AtomicInteger workerCount = new AtomicInteger(1);
/*  237 */     return new TracingExecutor(Executors.newCachedThreadPool(runnable -> {
/*  238 */             Thread thread = new Thread(runnable);
/*  239 */             String name = prefix + prefix;
/*  240 */             TracyClient.setThreadName(name, prefix.hashCode());
/*  241 */             thread.setName(name);
/*  242 */             thread.setDaemon(daemon);
/*  243 */             thread.setUncaughtExceptionHandler(Util::onThreadException);
/*  244 */             return thread;
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*  249 */   public static void throwAsRuntime(Throwable throwable) { throw (throwable instanceof RuntimeException) ? (RuntimeException)throwable : new RuntimeException(throwable); }
/*      */ 
/*      */   
/*      */   private static void onThreadException(Thread thread, Throwable throwable) {
/*  253 */     pauseInIde(throwable);
/*  254 */     if (throwable instanceof java.util.concurrent.CompletionException) {
/*  255 */       throwable = throwable.getCause();
/*      */     }
/*  257 */     if (throwable instanceof ReportedException) { ReportedException reportedException = (ReportedException)throwable;
/*  258 */       Bootstrap.realStdoutPrintln(reportedException.getReport().getFriendlyReport(ReportType.CRASH));
/*  259 */       System.exit(-1); }
/*      */     
/*  261 */     LOGGER.error("Caught exception in thread {}", thread, throwable);
/*      */   }
/*      */   
/*      */   public static Type<?> fetchChoiceType(DSL.TypeReference reference, String name) {
/*  265 */     if (!SharedConstants.CHECK_DATA_FIXER_SCHEMA) {
/*  266 */       return null;
/*      */     }
/*  268 */     return doFetchChoiceType(reference, name);
/*      */   }
/*      */   
/*      */   private static Type<?> doFetchChoiceType(DSL.TypeReference reference, String name) {
/*  272 */     Type<?> dataType = null;
/*      */     try {
/*  274 */       dataType = DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion().version())).getChoiceType(reference, name);
/*  275 */     } catch (IllegalArgumentException e) {
/*  276 */       LOGGER.error("No data fixer registered for {}", name);
/*  277 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  278 */         throw e;
/*      */       }
/*      */     } 
/*  281 */     return dataType;
/*      */   }
/*      */   
/*      */   public static void runNamed(Runnable runnable, String name) {
/*  285 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  286 */       thread = Thread.currentThread();
/*  287 */       oldName = thread.getName();
/*      */       
/*  289 */       thread.setName(name); 
/*  290 */       try { Zone ignored = TracyClient.beginZone(name, SharedConstants.IS_RUNNING_IN_IDE); 
/*  291 */         try { runnable.run();
/*  292 */           if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*  293 */             try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } finally { thread.setName(oldName); }
/*      */     
/*      */     } else {
/*  296 */       Zone ignored = TracyClient.beginZone(name, SharedConstants.IS_RUNNING_IN_IDE); 
/*  297 */       try { runnable.run();
/*  298 */         if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*      */           try { ignored.close(); }
/*      */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */             throw throwable; }
/*      */     
/*  303 */     }  } public static <T> String getRegisteredName(Registry<T> registry, T entry) { Identifier key = registry.getKey(entry);
/*  304 */     if (key == null) {
/*  305 */       return "[unregistered]";
/*      */     }
/*  307 */     return key.toString(); }
/*      */ 
/*      */ 
/*      */   
/*  311 */   public static <T> Predicate<T> allOf() { return context -> true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  316 */   public static <T> Predicate<T> allOf(Predicate<? super T> condition) { return condition; }
/*      */ 
/*      */ 
/*      */   
/*  320 */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2) { return context -> (condition1.test(context) && condition2.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  324 */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3) { return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  328 */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4) { return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context) && condition4.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  332 */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4, Predicate<? super T> condition5) { return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context) && condition4.test(context) && condition5.test(context)); }
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Predicate<T> allOf(Predicate... conditions) {
/*  337 */     return context -> {
/*  338 */         for (Predicate<? super T> entry : conditions) {
/*  339 */           if (!entry.test(context)) {
/*  340 */             return false;
/*      */           }
/*      */         } 
/*  343 */         return true;
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(List<? extends Predicate<? super T>> conditions) {
/*  348 */     switch (conditions.size()) { case 0: 
/*      */       case 1: 
/*      */       case 2:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 4:
/*      */       
/*      */       case 5:
/*  357 */        }  Predicate[] conditionsCopy = (Predicate[])conditions.toArray(x$0 -> new Predicate[x$0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  364 */   public static <T> Predicate<T> anyOf() { return context -> false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  369 */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1) { return condition1; }
/*      */ 
/*      */ 
/*      */   
/*  373 */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2) { return context -> (condition1.test(context) || condition2.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  377 */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3) { return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  381 */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4) { return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context) || condition4.test(context)); }
/*      */ 
/*      */ 
/*      */   
/*  385 */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4, Predicate<? super T> condition5) { return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context) || condition4.test(context) || condition5.test(context)); }
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Predicate<T> anyOf(Predicate... conditions) {
/*  390 */     return context -> {
/*  391 */         for (Predicate<? super T> entry : conditions) {
/*  392 */           if (entry.test(context)) {
/*  393 */             return true;
/*      */           }
/*      */         } 
/*  396 */         return false;
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(List<? extends Predicate<? super T>> conditions) {
/*  401 */     switch (conditions.size()) { case 0: 
/*      */       case 1: 
/*      */       case 2:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 4:
/*      */       
/*      */       case 5:
/*  410 */        }  Predicate[] conditionsCopy = (Predicate[])conditions.toArray(x$0 -> new Predicate[x$0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean isSymmetrical(int width, int height, List<T> ingredients) {
/*  417 */     if (width == 1) {
/*  418 */       return true;
/*      */     }
/*  420 */     int centerX = width / 2;
/*  421 */     for (int y = 0; y < height; y++) {
/*  422 */       for (int leftX = 0; leftX < centerX; leftX++) {
/*  423 */         int rightX = width - 1 - leftX;
/*  424 */         T left = (T)ingredients.get(leftX + y * width);
/*  425 */         T right = (T)ingredients.get(rightX + y * width);
/*  426 */         if (!left.equals(right)) {
/*  427 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  431 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  438 */   public static int growByHalf(int currentSize, int minimalNewSize) { return (int)Math.max(Math.min(currentSize + (currentSize >> 1), 2147483639L), minimalNewSize); }
/*      */ 
/*      */ 
/*      */   
/*      */   @SuppressForbidden(reason = "Intentional use of default locale for user-visible date")
/*  443 */   public static DateTimeFormatter localizedDateFormatter(FormatStyle formatStyle) { return DateTimeFormatter.ofLocalizedDateTime(formatStyle); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final enum OS
/*      */   {
/*      */     LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String telemetryName;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static  {
/*      */       // Byte code:
/*      */       //   0: new net/minecraft/util/Util$OS
/*      */       //   3: dup
/*      */       //   4: ldc 'LINUX'
/*      */       //   6: iconst_0
/*      */       //   7: ldc 'linux'
/*      */       //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*      */       //   12: putstatic net/minecraft/util/Util$OS.LINUX : Lnet/minecraft/util/Util$OS;
/*      */       //   15: new net/minecraft/util/Util$OS
/*      */       //   18: dup
/*      */       //   19: ldc 'SOLARIS'
/*      */       //   21: iconst_1
/*      */       //   22: ldc 'solaris'
/*      */       //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*      */       //   27: putstatic net/minecraft/util/Util$OS.SOLARIS : Lnet/minecraft/util/Util$OS;
/*      */       //   30: new net/minecraft/util/Util$OS$1
/*      */       //   33: dup
/*      */       //   34: ldc 'WINDOWS'
/*      */       //   36: iconst_2
/*      */       //   37: ldc 'windows'
/*      */       //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*      */       //   42: putstatic net/minecraft/util/Util$OS.WINDOWS : Lnet/minecraft/util/Util$OS;
/*      */       //   45: new net/minecraft/util/Util$OS$2
/*      */       //   48: dup
/*      */       //   49: ldc 'OSX'
/*      */       //   51: iconst_3
/*      */       //   52: ldc 'mac'
/*      */       //   54: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*      */       //   57: putstatic net/minecraft/util/Util$OS.OSX : Lnet/minecraft/util/Util$OS;
/*      */       //   60: new net/minecraft/util/Util$OS
/*      */       //   63: dup
/*      */       //   64: ldc 'UNKNOWN'
/*      */       //   66: iconst_4
/*      */       //   67: ldc 'unknown'
/*      */       //   69: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*      */       //   72: putstatic net/minecraft/util/Util$OS.UNKNOWN : Lnet/minecraft/util/Util$OS;
/*      */       //   75: invokestatic $values : ()[Lnet/minecraft/util/Util$OS;
/*      */       //   78: putstatic net/minecraft/util/Util$OS.$VALUES : [Lnet/minecraft/util/Util$OS;
/*      */       //   81: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #447	-> 0
/*      */       //   #448	-> 15
/*      */       //   #449	-> 30
/*      */       //   #455	-> 45
/*      */       //   #461	-> 60
/*      */       //   #446	-> 75
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  466 */     OS(String telemetryName) { this.telemetryName = telemetryName; }
/*      */ 
/*      */     
/*      */     public void openUri(URI uri) {
/*      */       try {
/*  471 */         Process process = Runtime.getRuntime().exec(getOpenUriArguments(uri));
/*  472 */         process.getInputStream().close();
/*  473 */         process.getErrorStream().close();
/*  474 */         process.getOutputStream().close();
/*  475 */       } catch (IOException e) {
/*  476 */         Util.LOGGER.error("Couldn't open location '{}'", uri, e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  481 */     public void openFile(File file) { openUri(file.toURI()); }
/*      */ 
/*      */ 
/*      */     
/*  485 */     public void openPath(Path path) { openUri(path.toUri()); }
/*      */ 
/*      */     
/*      */     protected String[] getOpenUriArguments(URI uri) {
/*  489 */       String string = uri.toString();
/*  490 */       if ("file".equals(uri.getScheme()))
/*      */       {
/*  492 */         string = string.replace("file:", "file://");
/*      */       }
/*  494 */       return new String[] { "xdg-open", string };
/*      */     }
/*      */     
/*      */     public void openUri(String uri) {
/*      */       try {
/*  499 */         openUri(new URI(uri));
/*      */       }
/*  501 */       catch (URISyntaxException|IllegalArgumentException e) {
/*  502 */         Util.LOGGER.error("Couldn't open uri '{}'", uri, e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  507 */     public String telemetryName() { return this.telemetryName; } } static enum null {
/*      */     protected String[] getOpenUriArguments(URI uri) { return new String[] { "rundll32", "url.dll,FileProtocolHandler", uri.toString() }; }
/*      */   }
/*      */   static enum null { protected String[] getOpenUriArguments(URI uri) { return new String[] { "open", uri.toString() }; } }
/*      */   public static OS getPlatform() {
/*  512 */     osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/*  513 */     if (osName.contains("win")) {
/*  514 */       return OS.WINDOWS;
/*      */     }
/*  516 */     if (osName.contains("mac")) {
/*  517 */       return OS.OSX;
/*      */     }
/*  519 */     if (osName.contains("solaris")) {
/*  520 */       return OS.SOLARIS;
/*      */     }
/*  522 */     if (osName.contains("sunos")) {
/*  523 */       return OS.SOLARIS;
/*      */     }
/*  525 */     if (osName.contains("linux")) {
/*  526 */       return OS.LINUX;
/*      */     }
/*  528 */     if (osName.contains("unix")) {
/*  529 */       return OS.LINUX;
/*      */     }
/*  531 */     return OS.UNKNOWN;
/*      */   }
/*      */   
/*      */   public static boolean isAarch64() {
/*  535 */     arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
/*  536 */     return arch.equals("aarch64");
/*      */   }
/*      */   
/*      */   public static URI parseAndValidateUntrustedUri(String uri) throws URISyntaxException {
/*  540 */     URI parsedUri = new URI(uri);
/*      */     
/*  542 */     String scheme = parsedUri.getScheme();
/*  543 */     if (scheme == null) {
/*  544 */       throw new URISyntaxException(uri, "Missing protocol in URI: " + uri);
/*      */     }
/*  546 */     String protocol = scheme.toLowerCase(Locale.ROOT);
/*  547 */     if (!ALLOWED_UNTRUSTED_LINK_PROTOCOLS.contains(protocol)) {
/*  548 */       throw new URISyntaxException(uri, "Unsupported protocol in URI: " + uri);
/*      */     }
/*      */     
/*  551 */     return parsedUri;
/*      */   }
/*      */   
/*      */   public static <T> T findNextInIterable(Iterable<T> collection, T current) {
/*  555 */     Iterator<T> iterator = collection.iterator();
/*  556 */     T first = (T)iterator.next();
/*      */     
/*  558 */     if (current != null) {
/*  559 */       T property = first;
/*      */       while (true) {
/*  561 */         if (property == current) {
/*  562 */           if (iterator.hasNext()) {
/*  563 */             return (T)iterator.next();
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*  568 */         if (iterator.hasNext()) {
/*  569 */           property = (T)iterator.next();
/*      */         }
/*      */       } 
/*      */     } 
/*  573 */     return first;
/*      */   }
/*      */   
/*      */   public static <T> T findPreviousInIterable(Iterable<T> collection, T current) {
/*  577 */     Iterator<T> iterator = collection.iterator();
/*  578 */     T last = null;
/*  579 */     while (iterator.hasNext()) {
/*  580 */       T next = (T)iterator.next();
/*  581 */       if (next == current) {
/*  582 */         if (last == null) {
/*  583 */           last = (T)(iterator.hasNext() ? Iterators.getLast(iterator) : current);
/*      */         }
/*      */         break;
/*      */       } 
/*  587 */       last = next;
/*      */     } 
/*  589 */     return last;
/*      */   }
/*      */ 
/*      */   
/*  593 */   public static <T> T make(Supplier<T> factory) { return (T)factory.get(); }
/*      */ 
/*      */   
/*      */   public static <T> T make(T t, Consumer<? super T> consumer) {
/*  597 */     consumer.accept(t);
/*  598 */     return t;
/*      */   }
/*      */   
/*      */   public static <K extends Enum<K>, V> Map<K, V> makeEnumMap(Class<K> keyType, Function<K, V> function) {
/*  602 */     EnumMap<K, V> map = new EnumMap<K, V>(keyType); Enum[] arrayOfEnum; int i; byte b;
/*  603 */     for (arrayOfEnum = (Enum[])keyType.getEnumConstants(), i = arrayOfEnum.length, b = 0; b < i; ) { K key = (K)arrayOfEnum[b];
/*  604 */       map.put(key, function.apply(key)); b++; }
/*      */     
/*  606 */     return map;
/*      */   }
/*      */ 
/*      */   
/*  610 */   public static <K, V1, V2> Map<K, V2> mapValues(Map<K, V1> map, Function<? super V1, V2> valueMapper) { return (Map)map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> valueMapper.apply(e.getValue()))); }
/*      */ 
/*      */ 
/*      */   
/*  614 */   public static <K, V1, V2> Map<K, V2> mapValuesLazy(Map<K, V1> map, Function<V1, V2> valueMapper) { return Maps.transformValues(map, valueMapper); }
/*      */ 
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequence(List<? extends CompletableFuture<V>> futures) {
/*  618 */     if (futures.isEmpty()) {
/*  619 */       return CompletableFuture.completedFuture(List.of());
/*      */     }
/*      */     
/*  622 */     if (futures.size() == 1)
/*      */     {
/*  624 */       return ((CompletableFuture)futures.getFirst()).thenApply(ObjectLists::singleton);
/*      */     }
/*      */     
/*  627 */     CompletableFuture<Void> all = CompletableFuture.allOf((CompletableFuture[])futures.toArray(new CompletableFuture[0]));
/*  628 */     return all.thenApply(ignored -> futures.stream().map(CompletableFuture::join).toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequenceFailFast(List<? extends CompletableFuture<? extends V>> futures) {
/*  633 */     CompletableFuture<List<V>> failureFuture = new CompletableFuture<List<V>>();
/*      */     
/*  635 */     Objects.requireNonNull(failureFuture); return fallibleSequence(futures, failureFuture::completeExceptionally)
/*  636 */       .applyToEither(failureFuture, Function.identity());
/*      */   }
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequenceFailFastAndCancel(List<? extends CompletableFuture<? extends V>> futures) {
/*  640 */     CompletableFuture<List<V>> failureFuture = new CompletableFuture<List<V>>();
/*      */     
/*  642 */     return fallibleSequence(futures, exception -> {
/*  643 */           if (failureFuture.completeExceptionally(exception)) {
/*  644 */             for (CompletableFuture<? extends V> future : futures) {
/*  645 */               future.cancel(true);
/*      */             }
/*      */           }
/*  648 */         }).applyToEither(failureFuture, Function.identity());
/*      */   }
/*      */   
/*      */   private static <V> CompletableFuture<List<V>> fallibleSequence(List<? extends CompletableFuture<? extends V>> futures, Consumer<Throwable> failureHandler) {
/*  652 */     ObjectArrayList<V> results = new ObjectArrayList<V>();
/*  653 */     results.size(futures.size());
/*  654 */     CompletableFuture[] decoratedFutures = new CompletableFuture[futures.size()];
/*      */     
/*  656 */     for (int i = 0; i < futures.size(); i++) {
/*  657 */       int index = i;
/*  658 */       decoratedFutures[i] = ((CompletableFuture)futures.get(i)).whenComplete((result, exception) -> {
/*  659 */             if (exception != null) {
/*  660 */               failureHandler.accept(exception);
/*      */             } else {
/*  662 */               results.set(index, result);
/*      */             } 
/*      */           });
/*      */     } 
/*      */     
/*  667 */     return CompletableFuture.allOf(decoratedFutures).thenApply(nothing -> results);
/*      */   }
/*      */   
/*      */   public static <T> Optional<T> ifElse(Optional<T> input, Consumer<T> onTrue, Runnable onFalse) {
/*  671 */     if (input.isPresent()) {
/*  672 */       onTrue.accept(input.get());
/*      */     } else {
/*  674 */       onFalse.run();
/*      */     } 
/*  676 */     return input;
/*      */   }
/*      */   
/*      */   public static <T> Supplier<T> name(final Supplier<T> task, Supplier<String> nameGetter) {
/*  680 */     if (SharedConstants.DEBUG_NAMED_RUNNABLES) {
/*  681 */       final String name = (String)nameGetter.get();
/*  682 */       return new Supplier<T>()
/*      */         {
/*      */           public T get() {
/*  685 */             return (T)task.get();
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  690 */             return name;
/*      */           }
/*      */         };
/*      */     } 
/*  694 */     return task;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Runnable name(final Runnable task, Supplier<String> nameGetter) {
/*  699 */     if (SharedConstants.DEBUG_NAMED_RUNNABLES) {
/*  700 */       final String name = (String)nameGetter.get();
/*  701 */       return new Runnable()
/*      */         {
/*      */           public void run() {
/*  704 */             task.run();
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  709 */             return name;
/*      */           }
/*      */         };
/*      */     } 
/*  713 */     return task;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void logAndPauseIfInIde(String message) {
/*  718 */     LOGGER.error(message);
/*  719 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  720 */       doPause(message);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void logAndPauseIfInIde(String message, Throwable throwable) {
/*  725 */     LOGGER.error(message, throwable);
/*  726 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  727 */       doPause(message);
/*      */     }
/*      */   }
/*      */   
/*      */   public static <T extends Throwable> T pauseInIde(T t) {
/*  732 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  733 */       LOGGER.error("Trying to throw a fatal exception, pausing in IDE", t);
/*  734 */       doPause(t.getMessage());
/*      */     } 
/*  736 */     return t;
/*      */   }
/*      */ 
/*      */   
/*  740 */   public static void setPause(Consumer<String> pauseFunction) { thePauser = pauseFunction; }
/*      */ 
/*      */   
/*      */   private static void doPause(String message) {
/*  744 */     Instant preLog = Instant.now();
/*      */     
/*  746 */     LOGGER.warn("Did you remember to set a breakpoint here?");
/*  747 */     boolean dontBotherWithPause = (Duration.between(preLog, Instant.now()).toMillis() > 500L);
/*  748 */     if (!dontBotherWithPause) {
/*  749 */       thePauser.accept(message);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String describeError(Throwable err) {
/*  754 */     if (err.getCause() != null)
/*  755 */       return describeError(err.getCause()); 
/*  756 */     if (err.getMessage() != null) {
/*  757 */       return err.getMessage();
/*      */     }
/*  759 */     return err.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  764 */   public static <T> T getRandom(T[] array, RandomSource random) { return array[random.nextInt(array.length)]; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  769 */   public static int getRandom(int[] array, RandomSource random) { return array[random.nextInt(array.length)]; }
/*      */ 
/*      */ 
/*      */   
/*  773 */   public static <T> T getRandom(List<T> list, RandomSource random) { return (T)list.get(random.nextInt(list.size())); }
/*      */ 
/*      */   
/*      */   public static <T> Optional<T> getRandomSafe(List<T> list, RandomSource random) {
/*  777 */     if (list.isEmpty()) {
/*  778 */       return Optional.empty();
/*      */     }
/*  780 */     return Optional.of(getRandom(list, random));
/*      */   }
/*      */   
/*      */   private static BooleanSupplier createRenamer(final Path from, final Path to) {
/*  784 */     return new BooleanSupplier()
/*      */       {
/*      */         public boolean getAsBoolean() {
/*      */           try {
/*  788 */             Files.move(from, to, new java.nio.file.CopyOption[0]);
/*  789 */             return true;
/*  790 */           } catch (IOException e) {
/*  791 */             Util.LOGGER.error("Failed to rename", e);
/*  792 */             return false;
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  798 */         public String toString() { return "rename " + String.valueOf(from) + " to " + String.valueOf(to); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static BooleanSupplier createDeleter(final Path target) {
/*  804 */     return new BooleanSupplier()
/*      */       {
/*      */         public boolean getAsBoolean() {
/*      */           try {
/*  808 */             Files.deleteIfExists(target);
/*  809 */             return true;
/*  810 */           } catch (IOException e) {
/*  811 */             Util.LOGGER.warn("Failed to delete", e);
/*  812 */             return false;
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  818 */         public String toString() { return "delete old " + String.valueOf(target); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static BooleanSupplier createFileDeletedCheck(final Path target) {
/*  824 */     return new BooleanSupplier()
/*      */       {
/*      */         public boolean getAsBoolean() {
/*  827 */           return !Files.exists(target, new java.nio.file.LinkOption[0]);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  832 */         public String toString() { return "verify that " + String.valueOf(target) + " is deleted"; }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static BooleanSupplier createFileCreatedCheck(final Path target) {
/*  838 */     return new BooleanSupplier()
/*      */       {
/*      */         public boolean getAsBoolean() {
/*  841 */           return Files.isRegularFile(target, new java.nio.file.LinkOption[0]);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  846 */         public String toString() { return "verify that " + String.valueOf(target) + " is present"; }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean executeInSequence(BooleanSupplier... operations) {
/*  852 */     for (BooleanSupplier operation : operations) {
/*  853 */       if (!operation.getAsBoolean()) {
/*  854 */         LOGGER.warn("Failed to execute {}", operation);
/*  855 */         return false;
/*      */       } 
/*      */     } 
/*  858 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean runWithRetries(int numberOfRetries, String description, BooleanSupplier... operations) {
/*  862 */     for (int retry = 0; retry < numberOfRetries; retry++) {
/*  863 */       if (executeInSequence(operations)) {
/*  864 */         return true;
/*      */       }
/*  866 */       LOGGER.error("Failed to {}, retrying {}/{}", new Object[] { description, Integer.valueOf(retry), Integer.valueOf(numberOfRetries) });
/*      */     } 
/*  868 */     LOGGER.error("Failed to {}, aborting, progress might be lost", description);
/*  869 */     return false;
/*      */   }
/*      */ 
/*      */   
/*  873 */   public static void safeReplaceFile(Path targetPath, Path newPath, Path backupPath) { safeReplaceOrMoveFile(targetPath, newPath, backupPath, false); }
/*      */ 
/*      */   
/*      */   public static boolean safeReplaceOrMoveFile(Path targetPath, Path newPath, Path backupPath, boolean noRollback) {
/*  877 */     if (Files.exists(targetPath, new java.nio.file.LinkOption[0]) && 
/*  878 */       !runWithRetries(10, "create backup " + String.valueOf(backupPath), new BooleanSupplier[] {
/*  879 */           createDeleter(backupPath), 
/*  880 */           createRenamer(targetPath, backupPath), 
/*  881 */           createFileCreatedCheck(backupPath)
/*      */         })) {
/*  883 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  888 */     if (!runWithRetries(10, "remove old " + String.valueOf(targetPath), new BooleanSupplier[] {
/*  889 */           createDeleter(targetPath), 
/*  890 */           createFileDeletedCheck(targetPath)
/*      */         })) {
/*  892 */       return false;
/*      */     }
/*      */     
/*  895 */     if (!runWithRetries(10, "replace " + String.valueOf(targetPath) + " with " + String.valueOf(newPath), new BooleanSupplier[] {
/*  896 */           createRenamer(newPath, targetPath), 
/*  897 */           createFileCreatedCheck(targetPath)
/*      */         }) && !noRollback) {
/*  899 */       runWithRetries(10, "restore " + String.valueOf(targetPath) + " from " + String.valueOf(backupPath), new BooleanSupplier[] {
/*  900 */             createRenamer(backupPath, targetPath), 
/*  901 */             createFileCreatedCheck(targetPath)
/*      */           });
/*  903 */       return false;
/*      */     } 
/*  905 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int offsetByCodepoints(String input, int pos, int offset) {
/*  910 */     int length = input.length();
/*  911 */     if (offset >= 0) {
/*  912 */       for (int i = 0; pos < length && i < offset; i++) {
/*  913 */         if (Character.isHighSurrogate(input.charAt(pos++)) && pos < length && 
/*  914 */           Character.isLowSurrogate(input.charAt(pos)))
/*      */         {
/*  916 */           pos++;
/*      */         }
/*      */       } 
/*      */     } else {
/*  920 */       for (int i = offset; pos > 0 && i < 0; i++) {
/*  921 */         if (Character.isLowSurrogate(input.charAt(--pos)) && pos > 0 && 
/*  922 */           Character.isHighSurrogate(input.charAt(pos - 1)))
/*      */         {
/*  924 */           pos--;
/*      */         }
/*      */       } 
/*      */     } 
/*  928 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*  932 */   public static Consumer<String> prefix(String prefix, Consumer<String> consumer) { return s -> consumer.accept(prefix + prefix); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DataResult<int[]> fixedSize(IntStream stream, int size) {
/*  939 */     int[] ints = stream.limit((size + 1)).toArray();
/*  940 */     if (ints.length != size) {
/*  941 */       Supplier<String> message = () -> "Input is not a list of " + size + " ints";
/*  942 */       if (ints.length >= size) {
/*  943 */         return DataResult.error(message, Arrays.copyOf(ints, size));
/*      */       }
/*  945 */       return DataResult.error(message);
/*      */     } 
/*      */     
/*  948 */     return DataResult.success(ints);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DataResult<long[]> fixedSize(LongStream stream, int size) {
/*  955 */     long[] longs = stream.limit((size + 1)).toArray();
/*  956 */     if (longs.length != size) {
/*  957 */       Supplier<String> message = () -> "Input is not a list of " + size + " longs";
/*  958 */       if (longs.length >= size) {
/*  959 */         return DataResult.error(message, Arrays.copyOf(longs, size));
/*      */       }
/*  961 */       return DataResult.error(message);
/*      */     } 
/*      */     
/*  964 */     return DataResult.success(longs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> DataResult<List<T>> fixedSize(List<T> list, int size) {
/*  971 */     if (list.size() != size) {
/*  972 */       Supplier<String> message = () -> "Input is not a list of " + size + " elements";
/*  973 */       if (list.size() >= size) {
/*  974 */         return DataResult.error(message, list.subList(0, size));
/*      */       }
/*  976 */       return DataResult.error(message);
/*      */     } 
/*      */     
/*  979 */     return DataResult.success(list);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void startTimerHackThread() {
/*  985 */     timerThread = new Thread("Timer hack thread")
/*      */       {
/*      */         public void run() {
/*      */           try {
/*      */             while (true)
/*  990 */               sleep(2147483647L); 
/*  991 */           } catch (InterruptedException e) {
/*  992 */             Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/*      */       };
/*  998 */     timerThread.setDaemon(true);
/*  999 */     timerThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 1000 */     timerThread.start();
/*      */   }
/*      */   
/*      */   public static void copyBetweenDirs(Path sourceDir, Path targetDir, Path sourcePath) {
/* 1004 */     Path relative = sourceDir.relativize(sourcePath);
/* 1005 */     Path target = targetDir.resolve(relative);
/* 1006 */     Files.copy(sourcePath, target, new java.nio.file.CopyOption[0]);
/*      */   }
/*      */ 
/*      */   
/* 1010 */   public static String sanitizeName(String value, CharPredicate isAllowedChar) { return (String)value.toLowerCase(Locale.ROOT).chars().mapToObj(c -> isAllowedChar.test((char)c) ? Character.toString((char)c) : "_").collect(Collectors.joining()); }
/*      */ 
/*      */ 
/*      */   
/* 1014 */   public static <K, V> SingleKeyCache<K, V> singleKeyCache(Function<K, V> computeValueFunction) { return new SingleKeyCache(computeValueFunction); }
/*      */ 
/*      */   
/*      */   public static <T, R> Function<T, R> memoize(final Function<T, R> function) {
/* 1018 */     return new Function<T, R>() {
/* 1019 */         private final Map<T, R> cache = new ConcurrentHashMap();
/*      */ 
/*      */ 
/*      */         
/* 1023 */         public R apply(T arg) { return (R)this.cache.computeIfAbsent(arg, function); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1028 */         public String toString() { return "memoize/1[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]"; }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<T, U, R> function) {
/* 1034 */     return new BiFunction<T, U, R>() {
/* 1035 */         private final Map<Pair<T, U>, R> cache = new ConcurrentHashMap();
/*      */ 
/*      */ 
/*      */         
/* 1039 */         public R apply(T a, U b) { return (R)this.cache.computeIfAbsent(Pair.of(a, b), args -> function.apply(args.getFirst(), args.getSecond())); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1044 */         public String toString() { return "memoize/2[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]"; }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> List<T> toShuffledList(Stream<T> stream, RandomSource random) {
/* 1050 */     ObjectArrayList<T> result = (ObjectArrayList)stream.collect(ObjectArrayList.toList());
/* 1051 */     shuffle(result, random);
/* 1052 */     return result;
/*      */   }
/*      */   
/*      */   public static IntArrayList toShuffledList(IntStream stream, RandomSource random) {
/* 1056 */     IntArrayList result = IntArrayList.wrap(stream.toArray());
/* 1057 */     int size = result.size();
/* 1058 */     for (int i = size; i > 1; i--) {
/* 1059 */       int swapTo = random.nextInt(i);
/* 1060 */       result.set(i - 1, result.set(swapTo, result.getInt(i - 1)));
/*      */     } 
/* 1062 */     return result;
/*      */   }
/*      */   
/*      */   public static <T> List<T> shuffledCopy(T[] array, RandomSource random) {
/* 1066 */     ObjectArrayList<T> copy = new ObjectArrayList<T>(array);
/* 1067 */     shuffle(copy, random);
/* 1068 */     return copy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<T> shuffledCopy(ObjectArrayList<T> list, RandomSource random) {
/* 1075 */     ObjectArrayList<T> copy = new ObjectArrayList<T>(list);
/* 1076 */     shuffle(copy, random);
/* 1077 */     return copy;
/*      */   }
/*      */   
/*      */   public static <T> void shuffle(List<T> list, RandomSource random) {
/* 1081 */     int size = list.size();
/* 1082 */     for (int i = size; i > 1; i--) {
/* 1083 */       int swapTo = random.nextInt(i);
/* 1084 */       list.set(i - 1, list.set(swapTo, list.get(i - 1)));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1089 */   public static <T> CompletableFuture<T> blockUntilDone(Function<Executor, CompletableFuture<T>> task) { return (CompletableFuture)blockUntilDone(task, CompletableFuture::isDone); }
/*      */ 
/*      */   
/*      */   public static <T> T blockUntilDone(Function<Executor, T> task, Predicate<T> completionCheck) {
/* 1093 */     BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
/*      */     
/* 1095 */     Objects.requireNonNull(tasks); T result = (T)task.apply(tasks::add);
/* 1096 */     while (!completionCheck.test(result)) {
/*      */       
/*      */       try {
/* 1099 */         Runnable runnable = (Runnable)tasks.poll(100L, TimeUnit.MILLISECONDS);
/* 1100 */         if (runnable != null) {
/* 1101 */           runnable.run();
/*      */         }
/* 1103 */       } catch (InterruptedException e) {
/* 1104 */         LOGGER.warn("Interrupted wait");
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1109 */     int remainingSize = tasks.size();
/* 1110 */     if (remainingSize > 0)
/*      */     {
/*      */       
/* 1113 */       LOGGER.warn("Tasks left in queue: {}", Integer.valueOf(remainingSize));
/*      */     }
/*      */     
/* 1116 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ToIntFunction<T> createIndexLookup(List<T> values) {
/* 1125 */     int size = values.size();
/* 1126 */     if (size < 8) {
/* 1127 */       Objects.requireNonNull(values); return values::indexOf;
/*      */     } 
/*      */     
/* 1130 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap(size);
/* 1131 */     object2IntOpenHashMap.defaultReturnValue(-1);
/* 1132 */     for (int i = 0; i < size; i++) {
/* 1133 */       object2IntOpenHashMap.put(values.get(i), i);
/*      */     }
/* 1135 */     return object2IntOpenHashMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ToIntFunction<T> createIndexIdentityLookup(List<T> values) {
/* 1144 */     int size = values.size();
/*      */     
/* 1146 */     if (size < 8) {
/* 1147 */       ReferenceImmutableList referenceImmutableList = new ReferenceImmutableList(values);
/* 1148 */       Objects.requireNonNull(referenceImmutableList); return referenceImmutableList::indexOf;
/*      */     } 
/*      */     
/* 1151 */     Reference2IntOpenHashMap reference2IntOpenHashMap = new Reference2IntOpenHashMap(size);
/* 1152 */     reference2IntOpenHashMap.defaultReturnValue(-1);
/* 1153 */     for (int i = 0; i < size; i++) {
/* 1154 */       reference2IntOpenHashMap.put(values.get(i), i);
/*      */     }
/* 1156 */     return reference2IntOpenHashMap;
/*      */   }
/*      */   
/*      */   public static <A, B> Typed<B> writeAndReadTypedOrThrow(Typed<A> typed, Type<B> newType, UnaryOperator<Dynamic<?>> function) {
/* 1160 */     Dynamic<?> dynamic = (Dynamic)typed.write().getOrThrow();
/*      */     
/* 1162 */     return readTypedOrThrow(newType, (Dynamic)function.apply(dynamic), true);
/*      */   }
/*      */ 
/*      */   
/* 1166 */   public static <T> Typed<T> readTypedOrThrow(Type<T> type, Dynamic<?> dynamic) { return readTypedOrThrow(type, dynamic, false); }
/*      */ 
/*      */   
/*      */   public static <T> Typed<T> readTypedOrThrow(Type<T> type, Dynamic<?> dynamic, boolean acceptPartial) {
/* 1170 */     DataResult<Typed<T>> result = type.readTyped(dynamic).map(Pair::getFirst);
/*      */     try {
/* 1172 */       if (acceptPartial) {
/* 1173 */         return (Typed)result.getPartialOrThrow(IllegalStateException::new);
/*      */       }
/* 1175 */       return (Typed)result.getOrThrow(IllegalStateException::new);
/* 1176 */     } catch (IllegalStateException e) {
/* 1177 */       CrashReport report = CrashReport.forThrowable(e, "Reading type");
/* 1178 */       CrashReportCategory category = report.addCategory("Info");
/* 1179 */       category.setDetail("Data", dynamic);
/* 1180 */       category.setDetail("Type", type);
/* 1181 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1186 */   public static <T> List<T> copyAndAdd(List<T> list, T element) { return ImmutableList.builderWithExpectedSize(list.size() + 1)
/* 1187 */       .addAll(list)
/* 1188 */       .add(element)
/* 1189 */       .build(); }
/*      */ 
/*      */ 
/*      */   
/* 1193 */   public static <T> List<T> copyAndAdd(T element, List<T> list) { return ImmutableList.builderWithExpectedSize(list.size() + 1)
/* 1194 */       .add(element)
/* 1195 */       .addAll(list)
/* 1196 */       .build(); }
/*      */ 
/*      */ 
/*      */   
/* 1200 */   public static <K, V> Map<K, V> copyAndPut(Map<K, V> map, K key, V value) { return ImmutableMap.builderWithExpectedSize(map.size() + 1)
/* 1201 */       .putAll(map)
/* 1202 */       .put(key, value)
/* 1203 */       .buildKeepingLast(); }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Util.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */