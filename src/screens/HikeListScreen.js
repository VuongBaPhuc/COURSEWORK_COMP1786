import React from 'react';
import { View, FlatList, Button, StyleSheet, Text } from 'react-native';
import HikeCard from '../components/HikeCard';

export default function HikeListScreen({ navigation, hikes }) {
  return (
    <View style={styles.container}>
      {hikes.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Text style={styles.emptyText}>No hikes yet. Add a new hike to get started.</Text>
          <Button title="Add Hike" onPress={() => navigation.navigate('AddHike')} />
        </View>
      ) : (
        <FlatList
          data={hikes}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <HikeCard
              hike={item}
              onPress={() => navigation.navigate('HikeDetail', { hikeId: item.id })}
            />
          )}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  emptyText: {
    fontSize: 16,
    marginBottom: 16,
    textAlign: 'center',
  },
});
