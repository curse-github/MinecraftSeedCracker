/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ import oshi.hardware.GlobalMemory;
/*     */ import oshi.hardware.GraphicsCard;
/*     */ import oshi.hardware.HardwareAbstractionLayer;
/*     */ import oshi.hardware.PhysicalMemory;
/*     */ import oshi.hardware.VirtualMemory;
/*     */ 
/*     */ public class SystemReport
/*     */ {
/*     */   public static final long BYTES_PER_MEBIBYTE = 1048576L;
/*     */   private static final long ONE_GIGA = 1000000000L;
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  32 */   private static final String OPERATING_SYSTEM = System.getProperty("os.name") + " (" + System.getProperty("os.name") + ") version " + System.getProperty("os.arch");
/*  33 */   private static final String JAVA_VERSION = System.getProperty("java.version") + ", " + System.getProperty("java.version");
/*  34 */   private static final String JAVA_VM_VERSION = System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.name") + "), " + System.getProperty("java.vm.info");
/*     */   
/*  36 */   public SystemReport() { this.entries = Maps.newLinkedHashMap();
/*     */ 
/*     */     
/*  39 */     setDetail("Minecraft Version", SharedConstants.getCurrentVersion().name());
/*  40 */     setDetail("Minecraft Version ID", SharedConstants.getCurrentVersion().id());
/*  41 */     setDetail("Operating System", OPERATING_SYSTEM);
/*  42 */     setDetail("Java Version", JAVA_VERSION);
/*  43 */     setDetail("Java VM Version", JAVA_VM_VERSION);
/*     */     
/*  45 */     setDetail("Memory", () -> {
/*  46 */           runtime = Runtime.getRuntime();
/*  47 */           long max = runtime.maxMemory();
/*  48 */           long total = runtime.totalMemory();
/*  49 */           long free = runtime.freeMemory();
/*  50 */           long maxMb = max / 1048576L;
/*  51 */           long totalMb = total / 1048576L;
/*  52 */           long freeMb = free / 1048576L;
/*     */           
/*  54 */           return "" + free + " bytes (" + free + " MiB) / " + freeMb + " bytes (" + total + " MiB) up to " + totalMb + " bytes (" + max + " MiB)";
/*     */         });
/*     */     
/*  57 */     setDetail("CPUs", () -> String.valueOf(Runtime.getRuntime().availableProcessors()));
/*     */     
/*  59 */     ignoreErrors("hardware", () -> putHardware(new SystemInfo()));
/*     */     
/*  61 */     setDetail("JVM Flags", () -> printJvmFlags(()));
/*     */     
/*  63 */     setDetail("Debug Flags", () -> printJvmFlags(())); }
/*     */   private final Map<String, String> entries;
/*     */   
/*     */   private static String printJvmFlags(Predicate<String> selector) {
/*  67 */     List<String> allArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
/*  68 */     List<String> selectedArguments = allArguments.stream().filter(selector).toList();
/*  69 */     return String.format(Locale.ROOT, "%d total; %s", new Object[] { Integer.valueOf(selectedArguments.size()), String.join(" ", selectedArguments) });
/*     */   }
/*     */ 
/*     */   
/*  73 */   public void setDetail(String key, String value) { this.entries.put(key, value); }
/*     */ 
/*     */   
/*     */   public void setDetail(String key, Supplier<String> valueSupplier) {
/*     */     try {
/*  78 */       setDetail(key, (String)valueSupplier.get());
/*  79 */     } catch (Exception e) {
/*  80 */       LOGGER.warn("Failed to get system info for {}", key, e);
/*  81 */       setDetail(key, "ERR");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putHardware(SystemInfo systemInfo) {
/*  86 */     HardwareAbstractionLayer hardware = systemInfo.getHardware();
/*  87 */     ignoreErrors("processor", () -> putProcessor(hardware.getProcessor()));
/*  88 */     ignoreErrors("graphics", () -> putGraphics(hardware.getGraphicsCards()));
/*  89 */     ignoreErrors("memory", () -> putMemory(hardware.getMemory()));
/*  90 */     ignoreErrors("storage", this::putStorage);
/*     */   }
/*     */   
/*     */   private void ignoreErrors(String group, Runnable action) {
/*     */     try {
/*  95 */       action.run();
/*  96 */     } catch (Throwable t) {
/*  97 */       LOGGER.warn("Failed retrieving info for group {}", group, t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 102 */   public static float sizeInMiB(long bytes) { return (float)bytes / 1048576.0F; }
/*     */ 
/*     */   
/*     */   private void putPhysicalMemory(List<PhysicalMemory> memoryPackages) {
/* 106 */     int memorySlot = 0;
/* 107 */     for (PhysicalMemory physicalMemory : memoryPackages) {
/* 108 */       String prefix = String.format(Locale.ROOT, "Memory slot #%d ", new Object[] { Integer.valueOf(memorySlot++) });
/* 109 */       setDetail(prefix + "capacity (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(physicalMemory.getCapacity())) }));
/* 110 */       setDetail(prefix + "clockSpeed (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)physicalMemory.getClockSpeed() / 1.0E9F) }));
/* 111 */       Objects.requireNonNull(physicalMemory); setDetail(prefix + "type", physicalMemory::getMemoryType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putVirtualMemory(VirtualMemory virtualMemory) {
/* 116 */     setDetail("Virtual memory max (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getVirtualMax())) }));
/* 117 */     setDetail("Virtual memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getVirtualInUse())) }));
/* 118 */     setDetail("Swap memory total (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getSwapTotal())) }));
/* 119 */     setDetail("Swap memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(virtualMemory.getSwapUsed())) }));
/*     */   }
/*     */   
/*     */   private void putMemory(GlobalMemory memory) {
/* 123 */     ignoreErrors("physical memory", () -> putPhysicalMemory(memory.getPhysicalMemory()));
/* 124 */     ignoreErrors("virtual memory", () -> putVirtualMemory(memory.getVirtualMemory()));
/*     */   }
/*     */   
/*     */   private void putGraphics(List<GraphicsCard> graphicsCards) {
/* 128 */     int gpuIndex = 0;
/* 129 */     for (GraphicsCard graphicsCard : graphicsCards) {
/* 130 */       String prefix = String.format(Locale.ROOT, "Graphics card #%d ", new Object[] { Integer.valueOf(gpuIndex++) });
/* 131 */       Objects.requireNonNull(graphicsCard); setDetail(prefix + "name", graphicsCard::getName);
/* 132 */       Objects.requireNonNull(graphicsCard); setDetail(prefix + "vendor", graphicsCard::getVendor);
/* 133 */       setDetail(prefix + "VRAM (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(graphicsCard.getVRam())) }));
/* 134 */       Objects.requireNonNull(graphicsCard); setDetail(prefix + "deviceId", graphicsCard::getDeviceId);
/* 135 */       Objects.requireNonNull(graphicsCard); setDetail(prefix + "versionInfo", graphicsCard::getVersionInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putProcessor(CentralProcessor processor) {
/* 140 */     CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
/*     */     
/* 142 */     Objects.requireNonNull(processorIdentifier); setDetail("Processor Vendor", processorIdentifier::getVendor);
/* 143 */     Objects.requireNonNull(processorIdentifier); setDetail("Processor Name", processorIdentifier::getName);
/* 144 */     Objects.requireNonNull(processorIdentifier); setDetail("Identifier", processorIdentifier::getIdentifier);
/* 145 */     Objects.requireNonNull(processorIdentifier); setDetail("Microarchitecture", processorIdentifier::getMicroarchitecture);
/* 146 */     setDetail("Frequency (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)processorIdentifier.getVendorFreq() / 1.0E9F) }));
/*     */     
/* 148 */     setDetail("Number of physical packages", () -> String.valueOf(processor.getPhysicalPackageCount()));
/* 149 */     setDetail("Number of physical CPUs", () -> String.valueOf(processor.getPhysicalProcessorCount()));
/* 150 */     setDetail("Number of logical CPUs", () -> String.valueOf(processor.getLogicalProcessorCount()));
/*     */   }
/*     */   
/*     */   private void putStorage() {
/* 154 */     putSpaceForProperty("jna.tmpdir");
/* 155 */     putSpaceForProperty("org.lwjgl.system.SharedLibraryExtractPath");
/* 156 */     putSpaceForProperty("io.netty.native.workdir");
/* 157 */     putSpaceForProperty("java.io.tmpdir");
/* 158 */     putSpaceForPath("workdir", () -> "");
/*     */   }
/*     */ 
/*     */   
/* 162 */   private void putSpaceForProperty(String env) { putSpaceForPath(env, () -> System.getProperty(env)); }
/*     */ 
/*     */   
/*     */   private void putSpaceForPath(String id, Supplier<String> pathSupplier) {
/* 166 */     String key = "Space in storage for " + id + " (MiB)";
/*     */     
/*     */     try {
/* 169 */       String path = (String)pathSupplier.get();
/* 170 */       if (path == null) {
/* 171 */         setDetail(key, "<path not set>");
/*     */         
/*     */         return;
/*     */       } 
/* 175 */       FileStore store = Files.getFileStore(Path.of(path, new String[0]));
/* 176 */       setDetail(key, String.format(Locale.ROOT, "available: %.2f, total: %.2f", new Object[] { Float.valueOf(sizeInMiB(store.getUsableSpace())), Float.valueOf(sizeInMiB(store.getTotalSpace())) }));
/* 177 */     } catch (InvalidPathException e) {
/* 178 */       LOGGER.warn("{} is not a path", id, e);
/* 179 */       setDetail(key, "<invalid path>");
/* 180 */     } catch (Exception e) {
/* 181 */       LOGGER.warn("Failed retrieving storage space for {}", id, e);
/* 182 */       setDetail(key, "ERR");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void appendToCrashReportString(StringBuilder sb) {
/* 187 */     sb.append("-- ").append("System Details").append(" --\n");
/* 188 */     sb.append("Details:");
/* 189 */     this.entries.forEach((key, value) -> {
/* 190 */           sb.append("\n\t");
/* 191 */           sb.append(key);
/* 192 */           sb.append(": ");
/* 193 */           sb.append(value);
/*     */         });
/*     */   }
/*     */   
/*     */   public String toLineSeparatedString() {
/* 198 */     return (String)this.entries.entrySet().stream()
/* 199 */       .map(e -> (String)e.getKey() + ": " + (String)e.getKey())
/* 200 */       .collect(Collectors.joining(System.lineSeparator()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\SystemReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */