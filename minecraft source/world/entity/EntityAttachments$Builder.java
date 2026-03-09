/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
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
/*    */ public class Builder
/*    */ {
/* 80 */   private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap(EntityAttachment.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   public Builder attach(EntityAttachment attachment, float x, float y, float z) { return attach(attachment, new Vec3(x, y, z)); }
/*    */ 
/*    */   
/*    */   public Builder attach(EntityAttachment attachment, Vec3 point) {
/* 90 */     ((List)this.attachments.computeIfAbsent(attachment, a -> new ArrayList(1))).add(point);
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public EntityAttachments build(float width, float height) {
/* 95 */     Map<EntityAttachment, List<Vec3>> attachments = Util.makeEnumMap(EntityAttachment.class, attachment -> {
/* 96 */           List<Vec3> points = (List)this.attachments.get(attachment);
/* 97 */           return (points == null) ? attachment.createFallbackPoints(width, height) : List.copyOf(points);
/*    */         });
/* 99 */     return new EntityAttachments(attachments);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityAttachments$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */