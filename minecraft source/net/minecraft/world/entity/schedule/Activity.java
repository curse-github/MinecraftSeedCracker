/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class Activity {
/*  7 */   public static final Activity CORE = register("core");
/*  8 */   public static final Activity IDLE = register("idle");
/*  9 */   public static final Activity WORK = register("work");
/* 10 */   public static final Activity PLAY = register("play");
/* 11 */   public static final Activity REST = register("rest");
/* 12 */   public static final Activity MEET = register("meet");
/* 13 */   public static final Activity PANIC = register("panic");
/* 14 */   public static final Activity RAID = register("raid");
/* 15 */   public static final Activity PRE_RAID = register("pre_raid");
/* 16 */   public static final Activity HIDE = register("hide");
/* 17 */   public static final Activity FIGHT = register("fight");
/* 18 */   public static final Activity CELEBRATE = register("celebrate");
/* 19 */   public static final Activity ADMIRE_ITEM = register("admire_item");
/* 20 */   public static final Activity AVOID = register("avoid");
/* 21 */   public static final Activity RIDE = register("ride");
/* 22 */   public static final Activity PLAY_DEAD = register("play_dead");
/* 23 */   public static final Activity LONG_JUMP = register("long_jump");
/* 24 */   public static final Activity RAM = register("ram");
/* 25 */   public static final Activity TONGUE = register("tongue");
/* 26 */   public static final Activity SWIM = register("swim");
/* 27 */   public static final Activity LAY_SPAWN = register("lay_spawn");
/* 28 */   public static final Activity SNIFF = register("sniff");
/* 29 */   public static final Activity INVESTIGATE = register("investigate");
/* 30 */   public static final Activity ROAR = register("roar");
/* 31 */   public static final Activity EMERGE = register("emerge");
/* 32 */   public static final Activity DIG = register("dig");
/*    */   
/*    */   private final String name;
/*    */   private final int hashCode;
/*    */   
/*    */   private Activity(String name) {
/* 38 */     this.name = name;
/* 39 */     this.hashCode = name.hashCode();
/*    */   }
/*    */ 
/*    */   
/* 43 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   private static Activity register(String name) { return (Activity)Registry.register(BuiltInRegistries.ACTIVITY, name, new Activity(name)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 52 */     if (this == o) {
/* 53 */       return true;
/*    */     }
/* 55 */     if (o == null || getClass() != o.getClass()) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     Activity activity = (Activity)o;
/*    */     
/* 61 */     return this.name.equals(activity.name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public int hashCode() { return this.hashCode; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public String toString() { return getName(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\schedule\Activity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */