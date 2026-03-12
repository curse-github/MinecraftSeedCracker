/*    */ package net.minecraft.network.syncher;public final class EntityDataAccessor<T> extends Record { private final int id;
/*    */   
/*  3 */   public EntityDataAccessor(int id, EntityDataSerializer<T> serializer) { this.id = id; this.serializer = serializer; } private final EntityDataSerializer<T> serializer; public int id() { return this.id; } public EntityDataSerializer<T> serializer() { return this.serializer; }
/*    */   
/*    */   public boolean equals(Object o) {
/*  6 */     if (this == o) {
/*  7 */       return true;
/*    */     }
/*  9 */     if (o == null || getClass() != o.getClass()) {
/* 10 */       return false;
/*    */     }
/*    */     
/* 13 */     EntityDataAccessor<?> that = (EntityDataAccessor)o;
/*    */     
/* 15 */     return (this.id == that.id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public int hashCode() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String toString() { return "<entity data: " + this.id + ">"; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\EntityDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */