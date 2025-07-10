/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ag$a$1
 *  ag$a$2
 *  com.google.common.base.Function
 *  com.google.common.base.Ticker
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.util.concurrent.ListeningExecutorService
 *  com.google.common.util.concurrent.MoreExecutors
 *  com.mojang.datafixers.DSL$TypeReference
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.jtracy.TracyClient
 *  com.mojang.jtracy.Zone
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.Dynamic
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ReferenceImmutableList
 *  it.unimi.dsi.fastutil.objects.ReferenceList
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.jtracy.TracyClient;
import com.mojang.jtracy.Zone;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceImmutableList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.Schemas;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Property;
import net.minecraft.util.CachedMapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.TimeSupplier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.crash.ReportType;
import net.minecraft.util.function.CharPredicate;
import net.minecraft.util.logging.UncaughtExceptionLogger;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.thread.NameableExecutor;
import org.slf4j.Logger;

public class Util {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int MAX_PARALLELISM = 255;
    private static final int BACKUP_ATTEMPTS = 10;
    private static final String MAX_BG_THREADS_PROPERTY = "max.bg.threads";
    private static final NameableExecutor MAIN_WORKER_EXECUTOR = Util.createWorker("Main");
    private static final NameableExecutor IO_WORKER_EXECUTOR = Util.createIoWorker("IO-Worker-", false);
    private static final NameableExecutor DOWNLOAD_WORKER_EXECUTOR = Util.createIoWorker("Download-", true);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);
    public static final int field_46220 = 8;
    private static final Set<String> SUPPORTED_URI_PROTOCOLS = Set.of("http", "https");
    public static final long field_45714 = 1000000L;
    public static TimeSupplier.Nanoseconds nanoTimeSupplier = System::nanoTime;
    public static final Ticker TICKER = new Ticker(){

        public long read() {
            return nanoTimeSupplier.getAsLong();
        }
    };
    public static final UUID NIL_UUID = new UUID(0L, 0L);
    public static final FileSystemProvider JAR_FILE_SYSTEM_PROVIDER = FileSystemProvider.installedProviders().stream().filter(fileSystemProvider -> fileSystemProvider.getScheme().equalsIgnoreCase("jar")).findFirst().orElseThrow(() -> new IllegalStateException("No jar file system provider found"));
    private static Consumer<String> missingBreakpointHandler = message -> {};

    public static <K, V> Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <T> Collector<T, ?, List<T>> toArrayList() {
        return Collectors.toCollection(Lists::newArrayList);
    }

    public static <T extends Comparable<T>> String getValueAsString(Property<T> property, Object value) {
        return property.name((Comparable)value);
    }

    public static String createTranslationKey(String type, @Nullable Identifier id) {
        if (id == null) {
            return type + ".unregistered_sadface";
        }
        return type + "." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }

    public static long getMeasuringTimeMs() {
        return Util.getMeasuringTimeNano() / 1000000L;
    }

    public static long getMeasuringTimeNano() {
        return nanoTimeSupplier.getAsLong();
    }

    public static long getEpochTimeMs() {
        return Instant.now().toEpochMilli();
    }

    public static String getFormattedCurrentTime() {
        return DATE_TIME_FORMATTER.format(ZonedDateTime.now());
    }

    private static NameableExecutor createWorker(final String name) {
        ForkJoinPool $$4;
        int $$1 = Util.getAvailableBackgroundThreads();
        if ($$1 <= 0) {
            ListeningExecutorService $$2 = MoreExecutors.newDirectExecutorService();
        } else {
            AtomicInteger $$3 = new AtomicInteger(1);
            $$4 = new ForkJoinPool($$1, pool -> {
                final String $$3 = "Worker-" + name + "-" + $$3.getAndIncrement();
                ForkJoinWorkerThread $$4 = new ForkJoinWorkerThread(pool){

                    @Override
                    protected void onStart() {
                        TracyClient.setThreadName((String)$$3, (int)name.hashCode());
                        super.onStart();
                    }

                    @Override
                    protected void onTermination(Throwable throwable) {
                        if (throwable != null) {
                            LOGGER.warn("{} died", (Object)this.getName(), (Object)throwable);
                        } else {
                            LOGGER.debug("{} shutdown", (Object)this.getName());
                        }
                        super.onTermination(throwable);
                    }
                };
                $$4.setName($$3);
                return $$4;
            }, Util::uncaughtExceptionHandler, true);
        }
        return new NameableExecutor($$4);
    }

    public static int getAvailableBackgroundThreads() {
        return MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, Util.getMaxBackgroundThreads());
    }

    private static int getMaxBackgroundThreads() {
        String $$0 = System.getProperty(MAX_BG_THREADS_PROPERTY);
        if ($$0 != null) {
            try {
                int $$1 = Integer.parseInt($$0);
                if ($$1 >= 1 && $$1 <= 255) {
                    return $$1;
                }
                LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", new Object[]{MAX_BG_THREADS_PROPERTY, $$0, 255});
            }
            catch (NumberFormatException $$2) {
                LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", new Object[]{MAX_BG_THREADS_PROPERTY, $$0, 255});
            }
        }
        return 255;
    }

    public static NameableExecutor getMainWorkerExecutor() {
        return MAIN_WORKER_EXECUTOR;
    }

    public static NameableExecutor getIoWorkerExecutor() {
        return IO_WORKER_EXECUTOR;
    }

    public static NameableExecutor getDownloadWorkerExecutor() {
        return DOWNLOAD_WORKER_EXECUTOR;
    }

    public static void shutdownExecutors() {
        MAIN_WORKER_EXECUTOR.shutdown(3L, TimeUnit.SECONDS);
        IO_WORKER_EXECUTOR.shutdown(3L, TimeUnit.SECONDS);
    }

    private static NameableExecutor createIoWorker(String namePrefix, boolean daemon) {
        AtomicInteger $$2 = new AtomicInteger(1);
        return new NameableExecutor(Executors.newCachedThreadPool(runnable -> {
            Thread $$4 = new Thread(runnable);
            String $$5 = namePrefix + $$2.getAndIncrement();
            TracyClient.setThreadName((String)$$5, (int)namePrefix.hashCode());
            $$4.setName($$5);
            $$4.setDaemon(daemon);
            $$4.setUncaughtExceptionHandler(Util::uncaughtExceptionHandler);
            return $$4;
        }));
    }

    public static void throwUnchecked(Throwable t) {
        throw t instanceof RuntimeException ? (RuntimeException)t : new RuntimeException(t);
    }

    private static void uncaughtExceptionHandler(Thread thread, Throwable t) {
        Util.getFatalOrPause(t);
        if (t instanceof CompletionException) {
            t = t.getCause();
        }
        if (t instanceof CrashException) {
            CrashException $$2 = (CrashException)t;
            Bootstrap.println($$2.getReport().asString(ReportType.MINECRAFT_CRASH_REPORT));
            System.exit(-1);
        }
        LOGGER.error(String.format(Locale.ROOT, "Caught exception in thread %s", thread), t);
    }

    @Nullable
    public static Type<?> getChoiceType(DSL.TypeReference typeReference, String id) {
        if (!SharedConstants.useChoiceTypeRegistrations) {
            return null;
        }
        return Util.getChoiceTypeInternal(typeReference, id);
    }

    @Nullable
    private static Type<?> getChoiceTypeInternal(DSL.TypeReference typeReference, String id) {
        Type $$2;
        block2: {
            $$2 = null;
            try {
                $$2 = Schemas.getFixer().getSchema(DataFixUtils.makeKey((int)SharedConstants.getGameVersion().dataVersion().id())).getChoiceType(typeReference, id);
            }
            catch (IllegalArgumentException $$3) {
                LOGGER.error("No data fixer registered for {}", (Object)id);
                if (!SharedConstants.isDevelopment) break block2;
                throw $$3;
            }
        }
        return $$2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void runInNamedZone(Runnable runnable, String name) {
        block16: {
            if (SharedConstants.isDevelopment) {
                Thread $$2 = Thread.currentThread();
                String $$3 = $$2.getName();
                $$2.setName(name);
                try (Zone $$4 = TracyClient.beginZone((String)name, (boolean)SharedConstants.isDevelopment);){
                    runnable.run();
                    break block16;
                }
                finally {
                    $$2.setName($$3);
                }
            }
            try (Zone $$5 = TracyClient.beginZone((String)name, (boolean)SharedConstants.isDevelopment);){
                runnable.run();
            }
        }
    }

    public static <T> String registryValueToString(Registry<T> registry, T value) {
        Identifier $$2 = registry.getId(value);
        if ($$2 == null) {
            return "[unregistered]";
        }
        return $$2.toString();
    }

    public static <T> Predicate<T> and() {
        return o -> true;
    }

    public static <T> Predicate<T> and(Predicate<? super T> a2) {
        return a2;
    }

    public static <T> Predicate<T> and(Predicate<? super T> a2, Predicate<? super T> b2) {
        return o -> a2.test(o) && b2.test(o);
    }

    public static <T> Predicate<T> and(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2) {
        return o -> a2.test(o) && b2.test(o) && c2.test(o);
    }

    public static <T> Predicate<T> and(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2, Predicate<? super T> d2) {
        return o -> a2.test(o) && b2.test(o) && c2.test(o) && d2.test(o);
    }

    public static <T> Predicate<T> and(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2, Predicate<? super T> d2, Predicate<? super T> e2) {
        return o -> a2.test(o) && b2.test(o) && c2.test(o) && d2.test(o) && e2.test(o);
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T> ... predicates) {
        return o -> {
            for (Predicate $$2 : predicates) {
                if ($$2.test(o)) continue;
                return false;
            }
            return true;
        };
    }

    public static <T> Predicate<T> allOf(List<? extends Predicate<? super T>> predicates) {
        return switch (predicates.size()) {
            case 0 -> Util.and();
            case 1 -> Util.and(predicates.get(0));
            case 2 -> Util.and(predicates.get(0), predicates.get(1));
            case 3 -> Util.and(predicates.get(0), predicates.get(1), predicates.get(2));
            case 4 -> Util.and(predicates.get(0), predicates.get(1), predicates.get(2), predicates.get(3));
            case 5 -> Util.and(predicates.get(0), predicates.get(1), predicates.get(2), predicates.get(3), predicates.get(4));
            default -> {
                Predicate[] $$1 = (Predicate[])predicates.toArray(Predicate[]::new);
                yield Util.and($$1);
            }
        };
    }

    public static <T> Predicate<T> or() {
        return o -> false;
    }

    public static <T> Predicate<T> or(Predicate<? super T> a2) {
        return a2;
    }

    public static <T> Predicate<T> or(Predicate<? super T> a2, Predicate<? super T> b2) {
        return o -> a2.test(o) || b2.test(o);
    }

    public static <T> Predicate<T> or(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2) {
        return o -> a2.test(o) || b2.test(o) || c2.test(o);
    }

    public static <T> Predicate<T> or(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2, Predicate<? super T> d2) {
        return o -> a2.test(o) || b2.test(o) || c2.test(o) || d2.test(o);
    }

    public static <T> Predicate<T> or(Predicate<? super T> a2, Predicate<? super T> b2, Predicate<? super T> c2, Predicate<? super T> d2, Predicate<? super T> e2) {
        return o -> a2.test(o) || b2.test(o) || c2.test(o) || d2.test(o) || e2.test(o);
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<? super T> ... predicates) {
        return o -> {
            for (Predicate $$2 : predicates) {
                if (!$$2.test(o)) continue;
                return true;
            }
            return false;
        };
    }

    public static <T> Predicate<T> anyOf(List<? extends Predicate<? super T>> predicates) {
        return switch (predicates.size()) {
            case 0 -> Util.or();
            case 1 -> Util.or(predicates.get(0));
            case 2 -> Util.or(predicates.get(0), predicates.get(1));
            case 3 -> Util.or(predicates.get(0), predicates.get(1), predicates.get(2));
            case 4 -> Util.or(predicates.get(0), predicates.get(1), predicates.get(2), predicates.get(3));
            case 5 -> Util.or(predicates.get(0), predicates.get(1), predicates.get(2), predicates.get(3), predicates.get(4));
            default -> {
                Predicate[] $$1 = (Predicate[])predicates.toArray(Predicate[]::new);
                yield Util.or($$1);
            }
        };
    }

    public static <T> boolean isSymmetrical(int width, int height, List<T> list) {
        if (width == 1) {
            return true;
        }
        int $$3 = width / 2;
        for (int $$4 = 0; $$4 < height; ++$$4) {
            for (int $$5 = 0; $$5 < $$3; ++$$5) {
                T $$8;
                int $$6 = width - 1 - $$5;
                T $$7 = list.get($$5 + $$4 * width);
                if ($$7.equals($$8 = list.get($$6 + $$4 * width))) continue;
                return false;
            }
        }
        return true;
    }

    public static int nextCapacity(int current, int min) {
        return (int)Math.max(Math.min((long)current + (long)(current >> 1), 0x7FFFFFF7L), (long)min);
    }

    public static OperatingSystem getOperatingSystem() {
        String $$0 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if ($$0.contains("win")) {
            return OperatingSystem.WINDOWS;
        }
        if ($$0.contains("mac")) {
            return OperatingSystem.OSX;
        }
        if ($$0.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        }
        if ($$0.contains("sunos")) {
            return OperatingSystem.SOLARIS;
        }
        if ($$0.contains("linux")) {
            return OperatingSystem.LINUX;
        }
        if ($$0.contains("unix")) {
            return OperatingSystem.LINUX;
        }
        return OperatingSystem.UNKNOWN;
    }

    public static URI validateUri(String uri) throws URISyntaxException {
        URI $$1 = new URI(uri);
        String $$2 = $$1.getScheme();
        if ($$2 == null) {
            throw new URISyntaxException(uri, "Missing protocol in URI: " + uri);
        }
        String $$3 = $$2.toLowerCase(Locale.ROOT);
        if (!SUPPORTED_URI_PROTOCOLS.contains($$3)) {
            throw new URISyntaxException(uri, "Unsupported protocol in URI: " + uri);
        }
        return $$1;
    }

    public static Stream<String> getJVMFlags() {
        RuntimeMXBean $$0 = ManagementFactory.getRuntimeMXBean();
        return $$0.getInputArguments().stream().filter(runtimeArg -> runtimeArg.startsWith("-X"));
    }

    public static <T> T getLast(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> T next(Iterable<T> iterable, @Nullable T object) {
        Iterator<T> $$2 = iterable.iterator();
        T $$3 = $$2.next();
        if (object != null) {
            T $$4 = $$3;
            while (true) {
                if ($$4 == object) {
                    if (!$$2.hasNext()) break;
                    return $$2.next();
                }
                if (!$$2.hasNext()) continue;
                $$4 = $$2.next();
            }
        }
        return $$3;
    }

    public static <T> T previous(Iterable<T> iterable, @Nullable T object) {
        Iterator<T> $$2 = iterable.iterator();
        T $$3 = null;
        while ($$2.hasNext()) {
            T $$4 = $$2.next();
            if ($$4 == object) {
                if ($$3 != null) break;
                $$3 = (T)($$2.hasNext() ? Iterators.getLast($$2) : object);
                break;
            }
            $$3 = $$4;
        }
        return $$3;
    }

    public static <T> T make(Supplier<T> factory) {
        return factory.get();
    }

    public static <T> T make(T object, Consumer<? super T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <K extends Enum<K>, V> Map<K, V> mapEnum(Class<K> enumClass, java.util.function.Function<K, V> mapper) {
        EnumMap<Enum, V> $$2 = new EnumMap<Enum, V>(enumClass);
        for (Enum $$3 : (Enum[])enumClass.getEnumConstants()) {
            $$2.put($$3, mapper.apply($$3));
        }
        return $$2;
    }

    public static <K, V1, V2> Map<K, V2> transformMapValues(Map<K, V1> map, java.util.function.Function<? super V1, V2> transformer) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> transformer.apply((Object)entry.getValue())));
    }

    public static <K, V1, V2> Map<K, V2> transformMapValuesLazy(Map<K, V1> map, Function<V1, V2> transformer) {
        return Maps.transformValues(map, transformer);
    }

    public static <V> CompletableFuture<List<V>> combineSafe(List<? extends CompletableFuture<V>> futures) {
        if (futures.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        if (futures.size() == 1) {
            return futures.get(0).thenApply(List::of);
        }
        CompletableFuture<Void> $$1 = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return $$1.thenApply(void_ -> futures.stream().map(CompletableFuture::join).toList());
    }

    public static <V> CompletableFuture<List<V>> combine(List<? extends CompletableFuture<? extends V>> futures) {
        CompletableFuture $$1 = new CompletableFuture();
        return Util.combine(futures, $$1::completeExceptionally).applyToEither((CompletionStage)$$1, java.util.function.Function.identity());
    }

    public static <V> CompletableFuture<List<V>> combineCancellable(List<? extends CompletableFuture<? extends V>> futures) {
        CompletableFuture $$1 = new CompletableFuture();
        return Util.combine(futures, throwable -> {
            if ($$1.completeExceptionally((Throwable)throwable)) {
                for (CompletableFuture $$3 : futures) {
                    $$3.cancel(true);
                }
            }
        }).applyToEither((CompletionStage)$$1, java.util.function.Function.identity());
    }

    private static <V> CompletableFuture<List<V>> combine(List<? extends CompletableFuture<? extends V>> futures, Consumer<Throwable> exceptionHandler) {
        ArrayList $$2 = Lists.newArrayListWithCapacity((int)futures.size());
        CompletableFuture[] $$3 = new CompletableFuture[futures.size()];
        futures.forEach(future -> {
            int $$4 = $$2.size();
            $$2.add(null);
            $$1[$$4] = future.whenComplete((value, throwable) -> {
                if (throwable != null) {
                    exceptionHandler.accept((Throwable)throwable);
                } else {
                    $$2.set($$4, value);
                }
            });
        });
        return CompletableFuture.allOf($$3).thenApply(void_ -> $$2);
    }

    public static <T> Optional<T> ifPresentOrElse(Optional<T> optional, Consumer<T> presentAction, Runnable elseAction) {
        if (optional.isPresent()) {
            presentAction.accept(optional.get());
        } else {
            elseAction.run();
        }
        return optional;
    }

    public static <T> Supplier<T> debugSupplier(Supplier<T> supplier, Supplier<String> messageSupplier) {
        return supplier;
    }

    public static Runnable debugRunnable(Runnable runnable, Supplier<String> messageSupplier) {
        return runnable;
    }

    public static void logErrorOrPause(String message) {
        LOGGER.error(message);
        if (SharedConstants.isDevelopment) {
            Util.pause(message);
        }
    }

    public static void logErrorOrPause(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
        if (SharedConstants.isDevelopment) {
            Util.pause(message);
        }
    }

    public static <T extends Throwable> T getFatalOrPause(T t) {
        if (SharedConstants.isDevelopment) {
            LOGGER.error("Trying to throw a fatal exception, pausing in IDE", t);
            Util.pause(t.getMessage());
        }
        return t;
    }

    public static void setMissingBreakpointHandler(Consumer<String> missingBreakpointHandler) {
        Util.missingBreakpointHandler = missingBreakpointHandler;
    }

    private static void pause(String message) {
        boolean $$2;
        Instant $$1 = Instant.now();
        LOGGER.warn("Did you remember to set a breakpoint here?");
        boolean bl = $$2 = Duration.between($$1, Instant.now()).toMillis() > 500L;
        if (!$$2) {
            missingBreakpointHandler.accept(message);
        }
    }

    public static String getInnermostMessage(Throwable t) {
        if (t.getCause() != null) {
            return Util.getInnermostMessage(t.getCause());
        }
        if (t.getMessage() != null) {
            return t.getMessage();
        }
        return t.toString();
    }

    public static <T> T getRandom(T[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

    public static int getRandom(int[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

    public static <T> T getRandom(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> Optional<T> getRandomOrEmpty(List<T> list, Random random) {
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Util.getRandom(list, random));
    }

    private static BooleanSupplier renameTask(final Path src, final Path dest) {
        return new BooleanSupplier(){

            @Override
            public boolean getAsBoolean() {
                try {
                    Files.move(src, dest, new CopyOption[0]);
                    return true;
                }
                catch (IOException $$0) {
                    LOGGER.error("Failed to rename", (Throwable)$$0);
                    return false;
                }
            }

            public String toString() {
                return "rename " + String.valueOf(src) + " to " + String.valueOf(dest);
            }
        };
    }

    private static BooleanSupplier deleteTask(final Path path) {
        return new BooleanSupplier(){

            @Override
            public boolean getAsBoolean() {
                try {
                    Files.deleteIfExists(path);
                    return true;
                }
                catch (IOException $$0) {
                    LOGGER.warn("Failed to delete", (Throwable)$$0);
                    return false;
                }
            }

            public String toString() {
                return "delete old " + String.valueOf(path);
            }
        };
    }

    private static BooleanSupplier deletionVerifyTask(final Path path) {
        return new BooleanSupplier(){

            @Override
            public boolean getAsBoolean() {
                return !Files.exists(path, new LinkOption[0]);
            }

            public String toString() {
                return "verify that " + String.valueOf(path) + " is deleted";
            }
        };
    }

    private static BooleanSupplier existenceCheckTask(final Path path) {
        return new BooleanSupplier(){

            @Override
            public boolean getAsBoolean() {
                return Files.isRegularFile(path, new LinkOption[0]);
            }

            public String toString() {
                return "verify that " + String.valueOf(path) + " is present";
            }
        };
    }

    private static boolean attemptTasks(BooleanSupplier ... tasks) {
        for (BooleanSupplier $$1 : tasks) {
            if ($$1.getAsBoolean()) continue;
            LOGGER.warn("Failed to execute {}", (Object)$$1);
            return false;
        }
        return true;
    }

    private static boolean attemptTasks(int retries, String taskName, BooleanSupplier ... tasks) {
        for (int $$3 = 0; $$3 < retries; ++$$3) {
            if (Util.attemptTasks(tasks)) {
                return true;
            }
            LOGGER.error("Failed to {}, retrying {}/{}", new Object[]{taskName, $$3, retries});
        }
        LOGGER.error("Failed to {}, aborting, progress might be lost", (Object)taskName);
        return false;
    }

    public static void backupAndReplace(Path current, Path newPath, Path backup) {
        Util.backupAndReplace(current, newPath, backup, false);
    }

    public static boolean backupAndReplace(Path current, Path newPath, Path backup, boolean noRestoreOnFail) {
        if (Files.exists(current, new LinkOption[0]) && !Util.attemptTasks(10, "create backup " + String.valueOf(backup), Util.deleteTask(backup), Util.renameTask(current, backup), Util.existenceCheckTask(backup))) {
            return false;
        }
        if (!Util.attemptTasks(10, "remove old " + String.valueOf(current), Util.deleteTask(current), Util.deletionVerifyTask(current))) {
            return false;
        }
        if (!Util.attemptTasks(10, "replace " + String.valueOf(current) + " with " + String.valueOf(newPath), Util.renameTask(newPath, current), Util.existenceCheckTask(current)) && !noRestoreOnFail) {
            Util.attemptTasks(10, "restore " + String.valueOf(current) + " from " + String.valueOf(backup), Util.renameTask(backup, current), Util.existenceCheckTask(current));
            return false;
        }
        return true;
    }

    public static int moveCursor(String string, int cursor, int delta) {
        int $$3 = string.length();
        if (delta >= 0) {
            for (int $$4 = 0; cursor < $$3 && $$4 < delta; ++$$4) {
                if (!Character.isHighSurrogate(string.charAt(cursor++)) || cursor >= $$3 || !Character.isLowSurrogate(string.charAt(cursor))) continue;
                ++cursor;
            }
        } else {
            for (int $$5 = delta; cursor > 0 && $$5 < 0; ++$$5) {
                if (!Character.isLowSurrogate(string.charAt(--cursor)) || cursor <= 0 || !Character.isHighSurrogate(string.charAt(cursor - 1))) continue;
                --cursor;
            }
        }
        return cursor;
    }

    public static Consumer<String> addPrefix(String prefix, Consumer<String> consumer) {
        return string -> consumer.accept(prefix + string);
    }

    public static DataResult<int[]> decodeFixedLengthArray(IntStream stream, int length) {
        int[] $$2 = stream.limit(length + 1).toArray();
        if ($$2.length != length) {
            Supplier<String> $$3 = () -> "Input is not a list of " + length + " ints";
            if ($$2.length >= length) {
                return DataResult.error($$3, (Object)Arrays.copyOf($$2, length));
            }
            return DataResult.error($$3);
        }
        return DataResult.success((Object)$$2);
    }

    public static DataResult<long[]> decodeFixedLengthArray(LongStream stream, int length) {
        long[] $$2 = stream.limit(length + 1).toArray();
        if ($$2.length != length) {
            Supplier<String> $$3 = () -> "Input is not a list of " + length + " longs";
            if ($$2.length >= length) {
                return DataResult.error($$3, (Object)Arrays.copyOf($$2, length));
            }
            return DataResult.error($$3);
        }
        return DataResult.success((Object)$$2);
    }

    public static <T> DataResult<List<T>> decodeFixedLengthList(List<T> list, int length) {
        if (list.size() != length) {
            Supplier<String> $$2 = () -> "Input is not a list of " + length + " elements";
            if (list.size() >= length) {
                return DataResult.error($$2, list.subList(0, length));
            }
            return DataResult.error($$2);
        }
        return DataResult.success(list);
    }

    public static void startTimerHack() {
        Thread $$0 = new Thread("Timer hack thread"){

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(Integer.MAX_VALUE);
                    }
                }
                catch (InterruptedException $$0) {
                    LOGGER.warn("Timer hack thread interrupted, that really should not happen");
                    return;
                }
            }
        };
        $$0.setDaemon(true);
        $$0.setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
        $$0.start();
    }

    public static void relativeCopy(Path src, Path dest, Path toCopy) throws IOException {
        Path $$3 = src.relativize(toCopy);
        Path $$4 = dest.resolve($$3);
        Files.copy(toCopy, $$4, new CopyOption[0]);
    }

    public static String replaceInvalidChars(String string, CharPredicate predicate) {
        return string.toLowerCase(Locale.ROOT).chars().mapToObj(charCode -> predicate.test((char)charCode) ? Character.toString((char)charCode) : "_").collect(Collectors.joining());
    }

    public static <K, V> CachedMapper<K, V> cachedMapper(java.util.function.Function<K, V> mapper) {
        return new CachedMapper<K, V>(mapper);
    }

    public static <T, R> java.util.function.Function<T, R> memoize(final java.util.function.Function<T, R> function) {
        return new java.util.function.Function<T, R>(){
            private final Map<T, R> cache = new ConcurrentHashMap();

            @Override
            public R apply(T object) {
                return this.cache.computeIfAbsent(object, function);
            }

            public String toString() {
                return "memoize/1[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]";
            }
        };
    }

    public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<T, U, R> biFunction) {
        return new BiFunction<T, U, R>(){
            private final Map<Pair<T, U>, R> cache = new ConcurrentHashMap();

            @Override
            public R apply(T a2, U b2) {
                return this.cache.computeIfAbsent(Pair.of(a2, b2), pair -> biFunction.apply(pair.getFirst(), pair.getSecond()));
            }

            public String toString() {
                return "memoize/2[function=" + String.valueOf(biFunction) + ", size=" + this.cache.size() + "]";
            }
        };
    }

    public static <T> List<T> copyShuffled(Stream<T> stream, Random random) {
        ObjectArrayList $$2 = (ObjectArrayList)stream.collect(ObjectArrayList.toList());
        Util.shuffle($$2, random);
        return $$2;
    }

    public static IntArrayList shuffle(IntStream stream, Random random) {
        int $$3;
        IntArrayList $$2 = IntArrayList.wrap((int[])stream.toArray());
        for (int $$4 = $$3 = $$2.size(); $$4 > 1; --$$4) {
            int $$5 = random.nextInt($$4);
            $$2.set($$4 - 1, $$2.set($$5, $$2.getInt($$4 - 1)));
        }
        return $$2;
    }

    public static <T> List<T> copyShuffled(T[] array, Random random) {
        ObjectArrayList $$2 = new ObjectArrayList((Object[])array);
        Util.shuffle($$2, random);
        return $$2;
    }

    public static <T> List<T> copyShuffled(ObjectArrayList<T> list, Random random) {
        ObjectArrayList $$2 = new ObjectArrayList(list);
        Util.shuffle($$2, random);
        return $$2;
    }

    public static <T> void shuffle(List<T> list, Random random) {
        int $$2;
        for (int $$3 = $$2 = list.size(); $$3 > 1; --$$3) {
            int $$4 = random.nextInt($$3);
            list.set($$3 - 1, list.set($$4, list.get($$3 - 1)));
        }
    }

    public static <T> CompletableFuture<T> waitAndApply(java.util.function.Function<Executor, CompletableFuture<T>> resultFactory) {
        return Util.waitAndApply(resultFactory, CompletableFuture::isDone);
    }

    public static <T> T waitAndApply(java.util.function.Function<Executor, T> resultFactory, Predicate<T> donePredicate) {
        int $$6;
        LinkedBlockingQueue $$2 = new LinkedBlockingQueue();
        T $$3 = resultFactory.apply($$2::add);
        while (!donePredicate.test($$3)) {
            try {
                Runnable $$4 = (Runnable)$$2.poll(100L, TimeUnit.MILLISECONDS);
                if ($$4 == null) continue;
                $$4.run();
            }
            catch (InterruptedException $$5) {
                LOGGER.warn("Interrupted wait");
                break;
            }
        }
        if (($$6 = $$2.size()) > 0) {
            LOGGER.warn("Tasks left in queue: {}", (Object)$$6);
        }
        return $$3;
    }

    public static <T> ToIntFunction<T> lastIndexGetter(List<T> values) {
        int $$1 = values.size();
        if ($$1 < 8) {
            return values::indexOf;
        }
        Object2IntOpenHashMap $$2 = new Object2IntOpenHashMap($$1);
        $$2.defaultReturnValue(-1);
        for (int $$3 = 0; $$3 < $$1; ++$$3) {
            $$2.put(values.get($$3), $$3);
        }
        return $$2;
    }

    public static <T> ToIntFunction<T> lastIdentityIndexGetter(List<T> values) {
        int $$1 = values.size();
        if ($$1 < 8) {
            ReferenceImmutableList $$2 = new ReferenceImmutableList(values);
            return arg_0 -> ((ReferenceList)$$2).indexOf(arg_0);
        }
        Reference2IntOpenHashMap $$3 = new Reference2IntOpenHashMap($$1);
        $$3.defaultReturnValue(-1);
        for (int $$4 = 0; $$4 < $$1; ++$$4) {
            $$3.put(values.get($$4), $$4);
        }
        return $$3;
    }

    public static <A, B> Typed<B> apply(Typed<A> typed, Type<B> type, UnaryOperator<Dynamic<?>> modifier) {
        Dynamic $$3 = (Dynamic)typed.write().getOrThrow();
        return Util.readTyped(type, (Dynamic)modifier.apply($$3), true);
    }

    public static <T> Typed<T> readTyped(Type<T> type, Dynamic<?> value) {
        return Util.readTyped(type, value, false);
    }

    public static <T> Typed<T> readTyped(Type<T> type, Dynamic<?> value, boolean allowPartial) {
        DataResult $$3 = type.readTyped(value).map(Pair::getFirst);
        try {
            if (allowPartial) {
                return (Typed)$$3.getPartialOrThrow(IllegalStateException::new);
            }
            return (Typed)$$3.getOrThrow(IllegalStateException::new);
        }
        catch (IllegalStateException $$4) {
            CrashReport $$5 = CrashReport.create($$4, "Reading type");
            CrashReportSection $$6 = $$5.addElement("Info");
            $$6.add("Data", value);
            $$6.add("Type", type);
            throw new CrashException($$5);
        }
    }

    public static <T> List<T> withAppended(List<T> list, T valueToAppend) {
        return ImmutableList.builderWithExpectedSize((int)(list.size() + 1)).addAll(list).add(valueToAppend).build();
    }

    public static <T> List<T> withPrepended(T valueToPrepend, List<T> list) {
        return ImmutableList.builderWithExpectedSize((int)(list.size() + 1)).add(valueToPrepend).addAll(list).build();
    }

    public static <K, V> Map<K, V> mapWith(Map<K, V> map, K keyToAppend, V valueToAppend) {
        return ImmutableMap.builderWithExpectedSize((int)(map.size() + 1)).putAll(map).put(keyToAppend, valueToAppend).buildKeepingLast();
    }

    public static sealed class OperatingSystem
    extends Enum<OperatingSystem>
    permits ag.a.1, ag.a.2 {
        public static final /* enum */ OperatingSystem LINUX = new OperatingSystem("linux");
        public static final /* enum */ OperatingSystem SOLARIS = new OperatingSystem("solaris");
        public static final /* enum */ OperatingSystem WINDOWS = new OperatingSystem("windows"){

            @Override
            protected String[] getURIOpenCommand(URI uri) {
                return new String[]{"rundll32", "url.dll,FileProtocolHandler", uri.toString()};
            }
        };
        public static final /* enum */ OperatingSystem OSX = new OperatingSystem("mac"){

            @Override
            protected String[] getURIOpenCommand(URI uri) {
                return new String[]{"open", uri.toString()};
            }
        };
        public static final /* enum */ OperatingSystem UNKNOWN = new OperatingSystem("unknown");
        private final String name;
        private static final /* synthetic */ OperatingSystem[] field_1136;

        public static OperatingSystem[] values() {
            return (OperatingSystem[])field_1136.clone();
        }

        public static OperatingSystem valueOf(String string) {
            return Enum.valueOf(OperatingSystem.class, string);
        }

        OperatingSystem(String name) {
            this.name = name;
        }

        public void open(URI uri) {
            try {
                Process $$1 = AccessController.doPrivileged(() -> Runtime.getRuntime().exec(this.getURIOpenCommand(uri)));
                $$1.getInputStream().close();
                $$1.getErrorStream().close();
                $$1.getOutputStream().close();
            }
            catch (IOException | PrivilegedActionException $$2) {
                LOGGER.error("Couldn't open location '{}'", (Object)uri, (Object)$$2);
            }
        }

        public void open(File file) {
            this.open(file.toURI());
        }

        public void open(Path path) {
            this.open(path.toUri());
        }

        protected String[] getURIOpenCommand(URI uri) {
            String $$1 = uri.toString();
            if ("file".equals(uri.getScheme())) {
                $$1 = $$1.replace("file:", "file://");
            }
            return new String[]{"xdg-open", $$1};
        }

        public void open(String uri) {
            try {
                this.open(new URI(uri));
            }
            catch (IllegalArgumentException | URISyntaxException $$1) {
                LOGGER.error("Couldn't open uri '{}'", (Object)uri, (Object)$$1);
            }
        }

        public String getName() {
            return this.name;
        }

        private static /* synthetic */ OperatingSystem[] method_36579() {
            return new OperatingSystem[]{LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN};
        }

        static {
            field_1136 = OperatingSystem.method_36579();
        }
    }
}

