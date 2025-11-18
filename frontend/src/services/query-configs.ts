import Request from "@/util/request";
import { QueryConfig } from "@/types";

class QueryConfigService {
  private prefix = "/query-configs";
  /**
   * Get all query configurations
   */
  list() {
    return Request.get(this.prefix);
  }

  /**
   * Get a specific query configuration by ID
   */
  get(id: string) {
    return Request.get(`${this.prefix}/${id}`);
  }

  /**
   * Get available database sources
   */
  getDbSources() {
    return Request.get(`${this.prefix}/data-sources`);
  }

  /**
   * Save a query configuration
   */
  save(item: QueryConfig): Promise<QueryConfig> {
    return Request.post(this.prefix, item) as Promise<QueryConfig>;
  }

  /**
   * Delete a query configuration by ID
   */
  remove(id: string) {
    return Request.delete(`${this.prefix}/${id}`);
  }

  /**
   * Check if a query configuration with the given ID exists
   */
  async exists(id: string): Promise<boolean> {
    try {
      const response = await Request.get(`${this.prefix}/${id}`);
      console.log("Config exists:", response);
      // 当 response 为空时，表示 id 不存在
      return response !== "" && response != null && response !== undefined;
    } catch (error) {
      console.error("Error checking if config exists:", error);
      return false;
    }
  }

  /**
   * Get parameters for a specific query configuration
   */
  getParams(id: string) {
    return Request.get(`${this.prefix}/params/${id}`);
  }

  /**
   * Save parameters for a query configuration
   */
  saveParams(params: any) {
    return Request.put(`${this.prefix}/params`, params);
  }

  /**
   * Delete cache for a specific query configuration
   */
  deleteCache(id: string) {
    return Request.delete(`${this.prefix}/cache/${id}`);
  }

  testQuery(dataSourceId: String, querySql: String) {
    return Request.post(`${this.prefix}/testQuery`, {
      sourceId: dataSourceId,
      querySql: querySql
    });
  }
}

export default new QueryConfigService();
