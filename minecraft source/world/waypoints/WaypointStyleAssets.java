/*    */ package net.minecraft.world.waypoints;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface WaypointStyleAssets {
/*  8 */   public static final ResourceKey<? extends Registry<WaypointStyleAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("waypoint_style_asset"));
/*    */   
/* 10 */   public static final ResourceKey<WaypointStyleAsset> DEFAULT = createId("default");
/* 11 */   public static final ResourceKey<WaypointStyleAsset> BOWTIE = createId("bowtie");
/*    */ 
/*    */   
/* 14 */   static ResourceKey<WaypointStyleAsset> createId(String name) { return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\WaypointStyleAssets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */