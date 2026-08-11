import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

export default function HomeScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>MHike</Text>
      <Text style={styles.subtitle}>Hike management application</Text>
      <View style={styles.buttonGroup}>
        <Button title="Hike List" onPress={() => navigation.navigate('HikeList')} />
        <Button title="Add New Hike" onPress={() => navigation.navigate('AddHike')} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 24,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 18,
    marginBottom: 24,
  },
  buttonGroup: {
    width: '100%',
    gap: 12,
  },
});
