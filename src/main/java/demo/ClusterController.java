package demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.persistence.Cluster;
import demo.persistence.ClusterRepository;

/**
 * Created by tharms on 26/09/15.
 */
@RestController
public class ClusterController {
    @Autowired
    private ClusterRepository clusterRepository;

    @RequestMapping("/cluster")
    public List<Cluster> getCluster() {
        return clusterRepository.getPersistedCluster();
    }
}
