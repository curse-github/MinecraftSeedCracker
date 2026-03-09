/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ public class TestFinder implements TestInstanceFinder, TestPosFinder {
/*  16 */   private static final TestInstanceFinder NO_FUNCTIONS = Stream::empty;
/*  17 */   private static final TestPosFinder NO_STRUCTURES = Stream::empty;
/*     */   
/*     */   private final TestInstanceFinder testInstanceFinder;
/*     */   
/*     */   private final TestPosFinder testPosFinder;
/*     */   private final CommandSourceStack source;
/*     */   
/*  24 */   public Stream<BlockPos> findTestPos() { return this.testPosFinder.findTestPos(); }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper;
/*     */     private final UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper;
/*     */     
/*     */     public Builder() {
/*  32 */       this.testFinderWrapper = (f -> f);
/*  33 */       this.structureBlockPosFinderWrapper = (f -> f);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Builder(UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper, UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper) {
/*  40 */       this.testFinderWrapper = testFinderWrapper;
/*  41 */       this.structureBlockPosFinderWrapper = structureBlockPosFinderWrapper;
/*     */     }
/*     */ 
/*     */     
/*  45 */     public Builder createMultipleCopies(int amount) { return new Builder(createCopies(amount), createCopies(amount)); }
/*     */ 
/*     */     
/*     */     private static <Q> UnaryOperator<Supplier<Stream<Q>>> createCopies(int amount) {
/*  49 */       return source -> {
/*  50 */           List<Q> copyList = new LinkedList<Q>();
/*  51 */           List<Q> sourceList = ((Stream)source.get()).toList();
/*  52 */           for (int i = 0; i < amount; i++) {
/*  53 */             copyList.addAll(sourceList);
/*     */           }
/*  55 */           Objects.requireNonNull(copyList); return copyList::stream;
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private TestFinder build(CommandSourceStack source, TestInstanceFinder testInstanceFinder, TestPosFinder testPosFinder) {
/*  62 */       Objects.requireNonNull(testInstanceFinder); Objects.requireNonNull((Supplier)this.testFinderWrapper.apply(testInstanceFinder::findTests));
/*  63 */       Objects.requireNonNull(testPosFinder); Objects.requireNonNull((Supplier)this.structureBlockPosFinderWrapper.apply(testPosFinder::findTestPos)); return new TestFinder(source, (Supplier)this.testFinderWrapper.apply(testInstanceFinder::findTests)::get, (Supplier)this.structureBlockPosFinderWrapper.apply(testPosFinder::findTestPos)::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public TestFinder radius(CommandContext<CommandSourceStack> sourceStack, int radius) {
/*  68 */       CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/*  69 */       BlockPos pos = BlockPos.containing(source.getPosition());
/*  70 */       return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(pos, radius, source.getLevel()));
/*     */     }
/*     */     
/*     */     public TestFinder nearest(CommandContext<CommandSourceStack> sourceStack) {
/*  74 */       CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/*  75 */       BlockPos pos = BlockPos.containing(source.getPosition());
/*  76 */       return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findNearestTest(pos, 15, source.getLevel()).stream());
/*     */     }
/*     */     
/*     */     public TestFinder allNearby(CommandContext<CommandSourceStack> sourceStack) {
/*  80 */       CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/*  81 */       BlockPos pos = BlockPos.containing(source.getPosition());
/*  82 */       return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(pos, 250, source.getLevel()));
/*     */     }
/*     */     
/*     */     public TestFinder lookedAt(CommandContext<CommandSourceStack> sourceStack) {
/*  86 */       CommandSourceStack source = (CommandSourceStack)sourceStack.getSource();
/*  87 */       return build(source, TestFinder.NO_FUNCTIONS, () -> StructureUtils.lookedAtTestPos(BlockPos.containing(source.getPosition()), source.getPlayer().getCamera(), source.getLevel()));
/*     */     }
/*     */ 
/*     */     
/*  91 */     public TestFinder failedTests(CommandContext<CommandSourceStack> sourceStack, boolean onlyRequiredTests) { return build((CommandSourceStack)sourceStack.getSource(), () -> FailedTestTracker.getLastFailedTests().filter(()), TestFinder.NO_STRUCTURES); }
/*     */ 
/*     */ 
/*     */     
/*  95 */     public TestFinder byResourceSelection(CommandContext<CommandSourceStack> sourceStack, Collection<Holder.Reference<GameTestInstance>> holders) { Objects.requireNonNull(holders); return build((CommandSourceStack)sourceStack.getSource(), holders::stream, TestFinder.NO_STRUCTURES); }
/*     */ 
/*     */ 
/*     */     
/*  99 */     public TestFinder failedTests(CommandContext<CommandSourceStack> sourceStack) { return failedTests(sourceStack, false); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */   
/*     */   private TestFinder(CommandSourceStack source, TestInstanceFinder testInstanceFinder, TestPosFinder testPosFinder) {
/* 108 */     this.source = source;
/* 109 */     this.testInstanceFinder = testInstanceFinder;
/* 110 */     this.testPosFinder = testPosFinder;
/*     */   }
/*     */ 
/*     */   
/* 114 */   public CommandSourceStack source() { return this.source; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public Stream<Holder.Reference<GameTestInstance>> findTests() { return this.testInstanceFinder.findTests(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */