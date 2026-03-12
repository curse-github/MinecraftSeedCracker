/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ public class GoalSelector
/*     */ {
/*  14 */   private static final WrappedGoal NO_GOAL = new WrappedGoal(2147483647, new Goal()
/*     */       {
/*     */         public boolean canUse() {
/*  17 */           return false;
/*     */         }
/*     */       })
/*     */     {
/*     */       public boolean isRunning() {
/*  22 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  26 */   private final Map<Goal.Flag, WrappedGoal> lockedFlags = new EnumMap(Goal.Flag.class);
/*  27 */   private final Set<WrappedGoal> availableGoals = new ObjectLinkedOpenHashSet();
/*  28 */   private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   public void addGoal(int prio, Goal goal) { this.availableGoals.add(new WrappedGoal(prio, goal)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public void removeAllGoals(Predicate<Goal> predicate) { this.availableGoals.removeIf(goal -> predicate.test(goal.getGoal())); }
/*     */ 
/*     */   
/*     */   public void removeGoal(Goal toRemove) {
/*  43 */     for (WrappedGoal availableGoal : this.availableGoals) {
/*  44 */       if (availableGoal.getGoal() == toRemove && availableGoal.isRunning()) {
/*  45 */         availableGoal.stop();
/*     */       }
/*     */     } 
/*  48 */     this.availableGoals.removeIf(goal -> (goal.getGoal() == toRemove));
/*     */   }
/*     */   
/*     */   private static boolean goalContainsAnyFlags(WrappedGoal goal, EnumSet<Goal.Flag> disabledFlags) {
/*  52 */     for (Goal.Flag flag : goal.getFlags()) {
/*  53 */       if (disabledFlags.contains(flag)) {
/*  54 */         return true;
/*     */       }
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean goalCanBeReplacedForAllFlags(WrappedGoal goal, Map<Goal.Flag, WrappedGoal> lockedFlags) {
/*  61 */     for (Goal.Flag flag : goal.getFlags()) {
/*  62 */       if (!((WrappedGoal)lockedFlags.getOrDefault(flag, NO_GOAL)).canBeReplacedBy(goal)) {
/*  63 */         return false;
/*     */       }
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  71 */     ProfilerFiller profiler = Profiler.get();
/*     */     
/*  73 */     profiler.push("goalCleanup");
/*  74 */     for (WrappedGoal goal : this.availableGoals) {
/*  75 */       if (goal.isRunning() && (goalContainsAnyFlags(goal, this.disabledFlags) || !goal.canContinueToUse())) {
/*  76 */         goal.stop();
/*     */       }
/*     */     } 
/*  79 */     this.lockedFlags.entrySet().removeIf(entry -> !((WrappedGoal)entry.getValue()).isRunning());
/*  80 */     profiler.pop();
/*     */     
/*  82 */     profiler.push("goalUpdate");
/*  83 */     for (WrappedGoal goal : this.availableGoals) {
/*  84 */       if (goal.isRunning() || goalContainsAnyFlags(goal, this.disabledFlags) || !goalCanBeReplacedForAllFlags(goal, this.lockedFlags) || !goal.canUse()) {
/*     */         continue;
/*     */       }
/*  87 */       for (Goal.Flag flag : goal.getFlags()) {
/*  88 */         WrappedGoal currentGoal = (WrappedGoal)this.lockedFlags.getOrDefault(flag, NO_GOAL);
/*  89 */         currentGoal.stop();
/*  90 */         this.lockedFlags.put(flag, goal);
/*     */       } 
/*  92 */       goal.start();
/*     */     } 
/*  94 */     profiler.pop();
/*     */     
/*  96 */     tickRunningGoals(true);
/*     */   }
/*     */   
/*     */   public void tickRunningGoals(boolean forceTickAllRunningGoals) {
/* 100 */     ProfilerFiller profiler = Profiler.get();
/*     */     
/* 102 */     profiler.push("goalTick");
/* 103 */     for (WrappedGoal goal : this.availableGoals) {
/* 104 */       if (goal.isRunning() && (forceTickAllRunningGoals || goal.requiresUpdateEveryTick())) {
/* 105 */         goal.tick();
/*     */       }
/*     */     } 
/* 108 */     profiler.pop();
/*     */   }
/*     */ 
/*     */   
/* 112 */   public Set<WrappedGoal> getAvailableGoals() { return this.availableGoals; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void disableControlFlag(Goal.Flag flag) { this.disabledFlags.add(flag); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public void enableControlFlag(Goal.Flag flag) { this.disabledFlags.remove(flag); }
/*     */ 
/*     */   
/*     */   public void setControlFlag(Goal.Flag flag, boolean enabled) {
/* 124 */     if (enabled) {
/* 125 */       enableControlFlag(flag);
/*     */     } else {
/* 127 */       disableControlFlag(flag);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\GoalSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */