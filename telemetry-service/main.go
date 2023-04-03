package main

import (
	"fmt"
	"github.com/amarchese96/sophos-telemetry-service/metrics"
	"github.com/gin-gonic/gin"
	"github.com/prometheus/common/model"
	"net/http"
)

func getAppsTraffic(c *gin.Context) {
	appGroupName := c.Query("app-group")
	appName := c.Query("app")
	direction := c.Query("direction")
	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if appName != "" {
		var results model.Vector
		var err error

		switch direction {
		case "inbound":
			results, _, err = metrics.GetAppInboundTraffic(appGroupName, appName, rangeWidth)
		case "outbound":
			results, _, err = metrics.GetAppOutboundTraffic(appGroupName, appName, rangeWidth)
		default:
			results, _, err = metrics.GetAppTraffic(appGroupName, appName, rangeWidth)
		}

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			trafficValues := map[string]float64{}
			for _, result := range results {
				if string(result.Metric["source_app"]) == appName {
					trafficValues[string(result.Metric["destination_app"])] = float64(result.Value)
				} else if string(result.Metric["destination_app"]) == appName {
					trafficValues[string(result.Metric["source_app"])] = float64(result.Value)
				}
			}
			c.IndentedJSON(http.StatusOK, trafficValues)
		}
	} else {
		results, _, err := metrics.GetAppsTraffic(appGroupName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			trafficValues := map[string]map[string]float64{}
			for _, result := range results {
				_, ok := trafficValues[string(result.Metric["source_app"])]
				if !ok {
					trafficValues[string(result.Metric["source_app"])] = map[string]float64{}
				}
				trafficValues[string(result.Metric["source_app"])][string(result.Metric["destination_app"])] = float64(result.Value)

				_, ok = trafficValues[string(result.Metric["destination_app"])]
				if !ok {
					trafficValues[string(result.Metric["destination_app"])] = map[string]float64{}
				}
				trafficValues[string(result.Metric["destination_app"])][string(result.Metric["source_app"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, trafficValues)
		}
	}
}

func getAppsCpuUsage(c *gin.Context) {
	appGroupName := c.Query("app-group")
	appName := c.Query("app")
	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if appName != "" {
		results, _, err := metrics.GetAppCpuUsage(appGroupName, appName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			if len(results) < 1 {
				c.IndentedJSON(http.StatusNotFound, fmt.Errorf("cpu usage metrics for app %s not found", appName))
			} else {
				c.IndentedJSON(http.StatusOK, float64(results[0].Value))
			}
		}
	} else {
		results, _, err := metrics.GetAppsCpuUsage(appGroupName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			cpuValues := map[string]float64{}
			for _, result := range results {
				cpuValues[string(result.Metric["container"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, cpuValues)
		}
	}
}

func getAppsMemoryUsage(c *gin.Context) {
	appGroupName := c.Query("app-group")
	appName := c.Query("app")
	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if appName != "" {
		results, _, err := metrics.GetAppMemoryUsage(appGroupName, appName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			if len(results) < 1 {
				c.IndentedJSON(http.StatusNotFound, fmt.Errorf("memory usage metrics for app %s not found", appName))
			} else {
				c.IndentedJSON(http.StatusOK, float64(results[0].Value))
			}
		}
	} else {
		results, _, err := metrics.GetAppsMemoryUsage(appGroupName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			memoryValues := map[string]float64{}
			for _, result := range results {
				memoryValues[string(result.Metric["container"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, memoryValues)
		}
	}
}

func getNodesLatencies(c *gin.Context) {
	nodeName := c.Query("node")

	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if nodeName != "" {
		latencyValues := map[string]float64{}
		results, _, err := metrics.GetNodeLatencies(nodeName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			for _, result := range results {
				latencyValues[string(result.Metric["destination_node"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, latencyValues)
		}
	} else {
		latencyValues := map[string]map[string]float64{}
		results, _, err := metrics.GetNodesLatencies(rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			for _, result := range results {
				_, ok := latencyValues[string(result.Metric["origin_node"])]
				if !ok {
					latencyValues[string(result.Metric["origin_node"])] = map[string]float64{}
				}
				latencyValues[string(result.Metric["origin_node"])][string(result.Metric["destination_node"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, latencyValues)
		}
	}
}

func getNodesAvailableMemory(c *gin.Context) {
	nodeName := c.Query("node")

	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if nodeName != "" {
		results, _, err := metrics.GetNodeAvailableMemory(nodeName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			if len(results) < 1 {
				c.IndentedJSON(http.StatusNotFound, fmt.Errorf("available memory metrics for node %s not found", nodeName))
			} else {
				c.IndentedJSON(http.StatusOK, float64(results[0].Value))
			}
		}
	} else {
		results, _, err := metrics.GetNodesAvailableMemory(rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			memoryValues := map[string]float64{}
			for _, result := range results {
				memoryValues[string(result.Metric["node_name"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, memoryValues)
		}
	}
}

func getNodesCpuUsage(c *gin.Context) {
	nodeName := c.Query("node")

	rangeWidth := c.Query("range-width")

	if rangeWidth == "" {
		rangeWidth = "5m"
	}

	if nodeName != "" {
		results, _, err := metrics.GetNodeCpuUsage(nodeName, rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			if len(results) < 1 {
				c.IndentedJSON(http.StatusNotFound, fmt.Errorf("cpu usage metrics for node %s not found", nodeName))
			} else {
				c.IndentedJSON(http.StatusOK, float64(results[0].Value))
			}
		}
	} else {
		results, _, err := metrics.GetNodesCpuUsage(rangeWidth)

		//fmt.Println(warnings)
		if err != nil {
			c.IndentedJSON(http.StatusInternalServerError, err)
		} else {
			cpuValues := map[string]float64{}
			for _, result := range results {
				cpuValues[string(result.Metric["node_name"])] = float64(result.Value)
			}
			c.IndentedJSON(http.StatusOK, cpuValues)
		}
	}
}

func main() {
	router := gin.Default()
	router.GET("/metrics/apps/traffic", getAppsTraffic)
	router.GET("/metrics/apps/cpu-usage", getAppsCpuUsage)
	router.GET("/metrics/apps/memory-usage", getAppsMemoryUsage)
	router.GET("/metrics/nodes/latencies", getNodesLatencies)
	router.GET("/metrics/nodes/available-memory", getNodesAvailableMemory)
	router.GET("/metrics/nodes/cpu-usage", getNodesCpuUsage)

	err := router.Run("0.0.0.0:8080")
	if err != nil {
		fmt.Printf("Exiting because of error: %s", err.Error())
		return
	}
}
