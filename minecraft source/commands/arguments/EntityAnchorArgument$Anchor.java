/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Anchor
/*    */ {
/* 60 */   FEET("feet", (p, e) -> p),
/* 61 */   EYES("eyes", (p, e) -> new Vec3(p.x, p.y + e.getEyeHeight(), p.z));
/*    */   
/*    */   static  {
/* 64 */     BY_NAME = (Map)Util.make(Maps.newHashMap(), map -> {
/* 65 */           for (Anchor anchor : values())
/* 66 */             map.put(anchor.name, anchor); 
/*    */         });
/*    */   }
/*    */   private static final Map<String, Anchor> BY_NAME;
/*    */   private final String name;
/*    */   private final BiFunction<Vec3, Entity, Vec3> transform;
/*    */   
/*    */   Anchor(String name, BiFunction<Vec3, Entity, Vec3> transform) {
/* 74 */     this.name = name;
/* 75 */     this.transform = transform;
/*    */   }
/*    */ 
/*    */   
/* 79 */   public static Anchor getByName(String name) { return (Anchor)BY_NAME.get(name); }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public Vec3 apply(Entity entity) { return (Vec3)this.transform.apply(entity.position(), entity); }
/*    */ 
/*    */   
/*    */   public Vec3 apply(CommandSourceStack source) {
/* 87 */     Entity entity = source.getEntity();
/* 88 */     if (entity == null) {
/* 89 */       return source.getPosition();
/*    */     }
/* 91 */     return (Vec3)this.transform.apply(source.getPosition(), entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\EntityAnchorArgument$Anchor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */