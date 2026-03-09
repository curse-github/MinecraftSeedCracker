/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.FileZipper;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.EmptyProfileResults;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PerfCommand
/*     */ {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  33 */   private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.perf.notRunning"));
/*  34 */   private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.perf.alreadyRunning"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  37 */     dispatcher.register(
/*  38 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("perf")
/*  39 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/*  40 */         .then(Commands.literal("start").executes(c -> startProfilingDedicatedServer((CommandSourceStack)c.getSource()))))
/*  41 */         .then(Commands.literal("stop").executes(c -> stopProfilingDedicatedServer((CommandSourceStack)c.getSource()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int startProfilingDedicatedServer(CommandSourceStack source) throws CommandSyntaxException {
/*  46 */     MinecraftServer server = source.getServer();
/*  47 */     if (server.isRecordingMetrics()) {
/*  48 */       throw ERROR_ALREADY_RUNNING.create();
/*     */     }
/*     */     
/*  51 */     Consumer<ProfileResults> onStopped = results -> whenStopped(source, results);
/*  52 */     Consumer<Path> onReportFinished = profilingLogs -> saveResults(source, profilingLogs, server);
/*     */     
/*  54 */     server.startRecordingMetrics(onStopped, onReportFinished);
/*  55 */     source.sendSuccess(() -> Component.translatable("commands.perf.started"), false);
/*  56 */     return 0;
/*     */   }
/*     */   
/*     */   private static int stopProfilingDedicatedServer(CommandSourceStack source) throws CommandSyntaxException {
/*  60 */     MinecraftServer server = source.getServer();
/*  61 */     if (!server.isRecordingMetrics()) {
/*  62 */       throw ERROR_NOT_RUNNING.create();
/*     */     }
/*     */     
/*  65 */     server.finishRecordingMetrics();
/*  66 */     return 0;
/*     */   }
/*     */   
/*     */   private static void saveResults(CommandSourceStack source, Path report, MinecraftServer server) {
/*  70 */     String zipFile, profilingName = String.format(Locale.ROOT, "%s-%s-%s", new Object[] {
/*  71 */           Util.getFilenameFormattedDateTime(), server
/*  72 */           .getWorldData().getLevelName(), 
/*  73 */           SharedConstants.getCurrentVersion().id()
/*     */         });
/*     */     
/*     */     try {
/*  77 */       zipFile = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, profilingName, ".zip");
/*  78 */     } catch (IOException e) {
/*  79 */       source.sendFailure(Component.translatable("commands.perf.reportFailed"));
/*  80 */       LOGGER.error("Failed to create report name", e);
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     fileZipper = new FileZipper(MetricsPersister.PROFILING_RESULTS_DIR.resolve(zipFile)); 
/*  85 */     try { fileZipper.add(Paths.get("system.txt", new String[0]), server.fillSystemReport(new SystemReport()).toLineSeparatedString());
/*  86 */       fileZipper.add(report);
/*  87 */       fileZipper.close(); } catch (Throwable throwable) { try { fileZipper.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  90 */      try { FileUtils.forceDelete(report.toFile()); }
/*  91 */     catch (IOException fileZipper)
/*  92 */     { IOException e; LOGGER.warn("Failed to delete temporary profiling file {}", report, e); }
/*     */ 
/*     */     
/*  95 */     source.sendSuccess(() -> Component.translatable("commands.perf.reportSaved", new Object[] { zipFile }), false);
/*     */   }
/*     */   
/*     */   private static void whenStopped(CommandSourceStack source, ProfileResults results) {
/*  99 */     if (results == EmptyProfileResults.EMPTY) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     int ticks = results.getTickDuration();
/* 104 */     double durationInSeconds = results.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
/* 105 */     source.sendSuccess(() -> Component.translatable("commands.perf.stopped", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(durationInSeconds) }), Integer.valueOf(ticks), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(ticks / durationInSeconds) }) }), false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\PerfCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */